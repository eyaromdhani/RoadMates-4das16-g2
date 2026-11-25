package tn.esprit.autoloc.reservation.repository;

import tn.esprit.autoloc.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByPassengerId(Long passengerId);
    List<Reservation> findByRideId(Long rideId);
}