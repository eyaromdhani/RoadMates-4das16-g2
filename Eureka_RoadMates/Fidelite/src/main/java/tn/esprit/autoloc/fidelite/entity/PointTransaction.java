package tn.esprit.autoloc.fidelite.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PointTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private LoyaltyAccount account;

    private int points;                 // négatif pour une REDEMPTION

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String rideId;

    @Column(unique = true)
    private UUID eventId;               // idempotence

    @ManyToOne
    @JoinColumn(name = "reward_id")
    private Reward reward;              // 0..1 : renseigné pour une REDEMPTION

    private BigDecimal creditAmountDT;

    private Instant createdAt = Instant.now();
}
