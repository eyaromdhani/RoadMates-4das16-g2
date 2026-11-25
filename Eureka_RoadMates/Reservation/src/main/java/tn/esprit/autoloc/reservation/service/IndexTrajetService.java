package tn.esprit.autoloc.reservation.service;

import tn.esprit.autoloc.reservation.entity.IndexTrajet;
import tn.esprit.autoloc.reservation.repository.IndexTrajetRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IndexTrajetService {
    private final IndexTrajetRepository repository;

    public IndexTrajetService(IndexTrajetRepository repository) {
        this.repository = repository;
    }

    public List<IndexTrajet> findAll() { return repository.findAll(); }

    public IndexTrajet findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trajet introuvable : " + id));
    }

    public List<IndexTrajet> search(String departure, String arrival, int seats) {
        return repository
                .findByDepartureIgnoreCaseAndArrivalIgnoreCaseAndSeatsLeftGreaterThanEqual(
                        departure, arrival, seats);
    }

    public IndexTrajet create(IndexTrajet t) { return repository.save(t); }

    public IndexTrajet update(Long id, IndexTrajet data) {
        IndexTrajet t = findById(id);
        t.setDriverId(data.getDriverId());
        t.setDeparture(data.getDeparture());
        t.setArrival(data.getArrival());
        t.setDepartAt(data.getDepartAt());
        t.setPrice(data.getPrice());
        t.setSeatsLeft(data.getSeatsLeft());
        return repository.save(t);
    }

    public void delete(Long id) { repository.deleteById(id); }
}