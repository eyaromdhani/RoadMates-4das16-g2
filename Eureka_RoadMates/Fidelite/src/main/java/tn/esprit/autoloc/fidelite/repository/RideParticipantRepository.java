package tn.esprit.autoloc.fidelite.repository;

import tn.esprit.autoloc.fidelite.entity.RideParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface RideParticipantRepository extends JpaRepository<RideParticipant, UUID> {
    List<RideParticipant> findByRideId(String rideId);
    List<RideParticipant> findByUserId(String userId);
}
