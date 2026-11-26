package tn.esprit.autoloc.trajets.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "rides")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Ride {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver_id", nullable = false)
    private UUID driverId;         // vient du JWT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Vehicle vehicle;

    @Column(name = "from_city", length = 60) private String fromCity;
    @Column(name = "to_city", length = 60)   private String toCity;
    @Column(name = "departure_datetime")     private LocalDateTime departureDatetime;
    @Column(name = "seats_total")            private Integer seatsTotal;
    @Column(name = "price_per_seat", precision = 8, scale = 2)
    private BigDecimal pricePerSeat;
    @Column(length = 12) private String status;
    @Column(name = "created_at") private LocalDateTime createdAt;

    @OneToMany(mappedBy = "ride", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RideStop> stops = new ArrayList<>();

    @OneToOne(mappedBy = "ride", cascade = CascadeType.ALL, orphanRemoval = true)
    private RideRecurrence recurrence;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); if (status == null) status = "OPEN"; }
}