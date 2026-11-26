package tn.esprit.autoloc.paiement.dto;

import tn.esprit.autoloc.paiement.entity.Wallet;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record WalletResponse(UUID id, String ownerId, BigDecimal balance, String currency, Instant createdAt) {
    public static WalletResponse from(Wallet wallet) {
        return new WalletResponse(wallet.getId(), wallet.getOwnerId(), wallet.getBalance(),
                wallet.getCurrency(), wallet.getCreatedAt());
    }
}
