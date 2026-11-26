package tn.esprit.autoloc.trajets.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Table(name = "vehicles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;          // vient du JWT

    @Column(length = 50)  private String brand;
    @Column(length = 50)  private String model;
    @Column(length = 30)  private String color;
    @Column(name = "plate_number", length = 20, unique = true)
    private String plateNumber;
    @Column(name = "seats_count") private Integer seatsCount;
}