package tn.esprit.autoloc.reservation.repository;

import tn.esprit.autoloc.reservation.entity.IndexTrajet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IndexTrajetRepository extends JpaRepository<IndexTrajet, Long> {
    List<IndexTrajet> findByDepartureIgnoreCaseAndArrivalIgnoreCaseAndSeatsLeftGreaterThanEqual(
            String departure, String arrival, int seats);
}