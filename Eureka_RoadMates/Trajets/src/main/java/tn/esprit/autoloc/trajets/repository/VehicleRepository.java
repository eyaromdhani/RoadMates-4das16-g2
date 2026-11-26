package tn.esprit.autoloc.trajets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.trajets.entity.Vehicle;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByOwnerId(UUID ownerId);
}