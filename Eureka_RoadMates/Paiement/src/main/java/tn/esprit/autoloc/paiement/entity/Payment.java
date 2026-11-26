package tn.esprit.autoloc.paiement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(length = 120)
    private String recipientId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.COMPLETED;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Payment() { }

    public Payment(Wallet wallet, String recipientId, BigDecimal amount, String description) {
        this.wallet = wallet;
        this.recipientId = recipientId;
        this.amount = amount;
        this.description = description;
    }

    public UUID getId() { return id; }
    public Wallet getWallet() { return wallet; }
    public String getRecipientId() { return recipientId; }
    public BigDecimal getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }
    public void updateDetails(String recipientId, String description) {
        this.recipientId = recipientId;
        this.description = description;
    }
    public void markRefunded() { this.status = PaymentStatus.REFUNDED; }
}
