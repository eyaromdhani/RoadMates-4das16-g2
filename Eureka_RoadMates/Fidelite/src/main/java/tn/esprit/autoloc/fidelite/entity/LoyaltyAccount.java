package tn.esprit.autoloc.fidelite.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoyaltyAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String userId;

    private int pointsBalance = 0;

    @Enumerated(EnumType.STRING)
    private Level level = Level.BRONZE;

    private Instant createdAt = Instant.now();

    @Version
    private Long version;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PointTransaction> historique = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserBadge> badges = new ArrayList<>();
}
