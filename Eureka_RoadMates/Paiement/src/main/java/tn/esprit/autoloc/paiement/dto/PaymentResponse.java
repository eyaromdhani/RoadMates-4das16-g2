package tn.esprit.autoloc.paiement.dto;

import tn.esprit.autoloc.paiement.entity.Payment;
import tn.esprit.autoloc.paiement.entity.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(UUID id, UUID walletId, String recipientId, BigDecimal amount,
                              PaymentStatus status, String description, Instant createdAt) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getWallet().getId(), payment.getRecipientId(),
                payment.getAmount(), payment.getStatus(), payment.getDescription(), payment.getCreatedAt());
    }
}
