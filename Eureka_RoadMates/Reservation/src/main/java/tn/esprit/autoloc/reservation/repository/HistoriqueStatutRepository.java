package tn.esprit.autoloc.reservation.repository;

import tn.esprit.autoloc.reservation.entity.HistoriqueStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoriqueStatutRepository extends JpaRepository<HistoriqueStatut, Long> {
    List<HistoriqueStatut> findByBookingIdOrderByChangedAtAsc(Long bookingId);
}