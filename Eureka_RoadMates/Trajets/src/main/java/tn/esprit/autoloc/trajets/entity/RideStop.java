package tn.esprit.autoloc.trajets.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "ride_stops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ride_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Ride ride;

    @Column(length = 60) private String city;
    @Column(name = "stop_order") private Integer stopOrder;
    @Column(name = "price_from_start", precision = 8, scale = 2)
    private java.math.BigDecimal priceFromStart;
}