package tn.esprit.autoloc.paiement.dto;

import tn.esprit.autoloc.paiement.entity.WalletTransaction;
import tn.esprit.autoloc.paiement.entity.WalletTransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record WalletTransactionResponse(UUID id, UUID walletId, UUID paymentId,
                                        WalletTransactionType type, BigDecimal amount,
                                        String description, Instant createdAt) {
    public static WalletTransactionResponse from(WalletTransaction transaction) {
        return new WalletTransactionResponse(transaction.getId(), transaction.getWallet().getId(),
                transaction.getPayment() == null ? null : transaction.getPayment().getId(),
                transaction.getType(), transaction.getAmount(), transaction.getDescription(),
                transaction.getCreatedAt());
    }
}
