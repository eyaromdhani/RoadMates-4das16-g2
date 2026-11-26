package tn.esprit.autoloc.trajets.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "ride_recurrences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideRecurrence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ride_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Ride ride;

    @Column(name = "days_of_week", length = 20) private String daysOfWeek;
    @Column(name = "start_date") private java.time.LocalDate startDate;
    @Column(name = "end_date")   private java.time.LocalDate endDate;
}