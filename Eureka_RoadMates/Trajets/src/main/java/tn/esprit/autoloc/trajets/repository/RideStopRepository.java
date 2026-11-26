package tn.esprit.autoloc.trajets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.trajets.entity.RideStop;

import java.util.List;

public interface RideStopRepository extends JpaRepository<RideStop, Long> {
    List<RideStop> findByRideIdOrderByStopOrderAsc(Long rideId);
}