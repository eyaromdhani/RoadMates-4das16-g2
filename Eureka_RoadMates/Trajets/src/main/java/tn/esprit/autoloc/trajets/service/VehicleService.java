package tn.esprit.autoloc.trajets.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.trajets.entity.Vehicle;
import tn.esprit.autoloc.trajets.repository.VehicleRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository repo;

    public Vehicle create(Vehicle v) {
        v.setId(null);
        return repo.save(v);
    }

    public List<Vehicle> findAll() {
        return repo.findAll();
    }

    public List<Vehicle> findByOwner(UUID ownerId) {
        return repo.findByOwnerId(ownerId);
    }

    public Vehicle findById(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found: " + id));
    }

    public Vehicle update(Long id, Vehicle data) {
        Vehicle v = findById(id);
        v.setBrand(data.getBrand());
        v.setModel(data.getModel());
        v.setColor(data.getColor());
        v.setPlateNumber(data.getPlateNumber());
        v.setSeatsCount(data.getSeatsCount());
        return repo.save(v);
    }

    public void delete(Long id) {
        repo.delete(findById(id));
    }
}