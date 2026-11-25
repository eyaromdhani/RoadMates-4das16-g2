package tn.esprit.autoloc.fidelite.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private LoyaltyAccount account;

    @ManyToOne
    @JoinColumn(name = "badge_id")
    private Badge badge;

    private Instant awardedAt = Instant.now();
}
