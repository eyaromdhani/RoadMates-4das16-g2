package tn.esprit.autoloc.reservation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class IndexTrajet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long driverId;
    private String departure;
    private String arrival;
    private LocalDateTime departAt;
    private double price;
    private int seatsLeft;

    @OneToMany(mappedBy = "ride")
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();
}