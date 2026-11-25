package tn.esprit.autoloc.fidelite.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RideParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // simples String : pas de @ManyToOne, pas de clé étrangère (alimenté par BookingConfirmed)
    private String rideId;
    private String userId;

    @Enumerated(EnumType.STRING)
    private Role role;
}
