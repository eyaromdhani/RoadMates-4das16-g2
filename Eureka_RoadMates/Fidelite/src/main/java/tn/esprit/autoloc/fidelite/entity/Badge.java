package tn.esprit.autoloc.fidelite.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private BadgeConditionType conditionType;

    private int threshold;
}
