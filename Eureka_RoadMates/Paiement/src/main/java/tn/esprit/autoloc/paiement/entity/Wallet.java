package tn.esprit.autoloc.paiement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 120)
    private String ownerId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO.setScale(2);

    @Column(nullable = false, length = 3)
    private String currency = "TND";

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Version
    private Long version;

    protected Wallet() { }

    public Wallet(String ownerId, BigDecimal balance) {
        this.ownerId = ownerId;
        this.balance = balance;
    }

    public UUID getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
    public Instant getCreatedAt() { return createdAt; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
