package tn.esprit.autoloc.reservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private IndexTrajet ride;          // rideId dans le diagramme

    private Long passengerId;
    private int seats;

    @Enumerated(EnumType.STRING)
    private Status status = Status.EN_ATTENTE;

    private LocalDateTime requestedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<HistoriqueStatut> historique = new ArrayList<>();
}