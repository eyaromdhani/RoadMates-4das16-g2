package tn.esprit.autoloc.paiement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PaymentCrudIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createsReadsUpdatesAndRefundsPaymentWithoutLosingLedgerHistory() throws Exception {
        String ownerId = "owner-" + UUID.randomUUID();
        createWallet(ownerId, "100.00");

        MvcResult created = mvc.perform(post("/payments")
                        .header("X-User-Id", ownerId)
                        .contentType("application/json")
                        .content("""
                                {"recipientId":"driver-2","amount":25.50,"description":"Ride booking"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        String paymentId = read(created).path("id").asText();

        JsonNode walletAfterPayment = getWallet(ownerId);
        assertThat(walletAfterPayment.path("balance").decimalValue()).isEqualByComparingTo("74.50");

        JsonNode payment = read(mvc.perform(get("/payments/{paymentId}", paymentId)
                        .header("X-User-Id", ownerId))
                .andExpect(status().isOk()).andReturn());
        assertThat(payment.path("status").asText()).isEqualTo("COMPLETED");
        assertThat(payment.path("amount").decimalValue()).isEqualByComparingTo("25.50");

        JsonNode history = read(mvc.perform(get("/payments").header("X-User-Id", ownerId))
                .andExpect(status().isOk()).andReturn());
        assertThat(history.size()).isEqualTo(1);

        JsonNode updated = read(mvc.perform(put("/payments/{paymentId}", paymentId)
                        .header("X-User-Id", ownerId)
                        .contentType("application/json")
                        .content("""
                                {"recipientId":"driver-3","description":"Confirmed ride"}
                                """))
                .andExpect(status().isOk()).andReturn());
        assertThat(updated.path("recipientId").asText()).isEqualTo("driver-3");
        assertThat(updated.path("description").asText()).isEqualTo("Confirmed ride");
        assertThat(updated.path("amount").decimalValue()).isEqualByComparingTo("25.50");

        JsonNode refunded = read(mvc.perform(delete("/payments/{paymentId}", paymentId)
                        .header("X-User-Id", ownerId))
                .andExpect(status().isOk()).andReturn());
        assertThat(refunded.path("status").asText()).isEqualTo("REFUNDED");
        assertThat(getWallet(ownerId).path("balance").decimalValue()).isEqualByComparingTo("100.00");

        JsonNode transactions = read(mvc.perform(get("/transactions").header("X-User-Id", ownerId))
                .andExpect(status().isOk()).andReturn());
        List<String> types = new ArrayList<>();
        transactions.forEach(transaction -> types.add(transaction.path("type").asText()));
        assertThat(types).contains("CREDIT", "PAYMENT", "REFUND");

        // A retry must not refund the same payment twice.
        mvc.perform(delete("/payments/{paymentId}", paymentId).header("X-User-Id", ownerId))
                .andExpect(status().isOk());
        assertThat(getWallet(ownerId).path("balance").decimalValue()).isEqualByComparingTo("100.00");
        assertThat(read(mvc.perform(get("/transactions").header("X-User-Id", ownerId))
                .andExpect(status().isOk()).andReturn()).size()).isEqualTo(3);
    }

    @Test
    void rejectsInvalidAndUnaffordablePaymentsWithoutChangingBalance() throws Exception {
        String ownerId = "owner-" + UUID.randomUUID();
        createWallet(ownerId, "5.00");

        mvc.perform(post("/payments")
                        .header("X-User-Id", ownerId)
                        .contentType("application/json")
                        .content("""
                                {"recipientId":"driver-2","amount":7.00,"description":"Too expensive"}
                                """))
                .andExpect(status().isPaymentRequired());

        mvc.perform(post("/payments")
                        .header("X-User-Id", ownerId)
                        .contentType("application/json")
                        .content("""
                                {"recipientId":"driver-2","amount":0,"description":"Invalid"}
                                """))
                .andExpect(status().isBadRequest());

        assertThat(getWallet(ownerId).path("balance").decimalValue()).isEqualByComparingTo("5.00");
        assertThat(read(mvc.perform(get("/payments").header("X-User-Id", ownerId))
                .andExpect(status().isOk()).andReturn()).size()).isZero();
    }

    @Test
    void paymentIsNotVisibleToAnotherOwner() throws Exception {
        String ownerId = "owner-" + UUID.randomUUID();
        createWallet(ownerId, "20.00");
        MvcResult created = mvc.perform(post("/payments")
                        .header("X-User-Id", ownerId)
                        .contentType("application/json")
                        .content("""
                                {"recipientId":"driver-2","amount":5.00,"description":"Private"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        String paymentId = read(created).path("id").asText();

        mvc.perform(get("/payments/{paymentId}", paymentId)
                        .header("X-User-Id", "another-owner"))
                .andExpect(status().isNotFound());
    }

    private void createWallet(String ownerId, String balance) throws Exception {
        mvc.perform(post("/wallets")
                        .contentType("application/json")
                        .content("""
                                {"ownerId":"%s","initialBalance":%s}
                                """.formatted(ownerId, balance)))
                .andExpect(status().isCreated());
    }

    private JsonNode getWallet(String ownerId) throws Exception {
        return read(mvc.perform(get("/wallets/me").header("X-User-Id", ownerId))
                .andExpect(status().isOk()).andReturn());
    }

    private JsonNode read(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }
}
