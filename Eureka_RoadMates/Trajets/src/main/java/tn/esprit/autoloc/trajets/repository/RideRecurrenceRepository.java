package tn.esprit.autoloc.trajets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.trajets.entity.RideRecurrence;

import java.util.Optional;

public interface RideRecurrenceRepository extends JpaRepository<RideRecurrence, Long> {
    Optional<RideRecurrence> findByRideId(Long rideId);
}