package tn.esprit.autoloc.trajets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.trajets.entity.Ride;

import java.util.List;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByDriverId(UUID driverId);
    List<Ride> findByFromCityIgnoreCaseAndToCityIgnoreCase(String fromCity, String toCity);
}