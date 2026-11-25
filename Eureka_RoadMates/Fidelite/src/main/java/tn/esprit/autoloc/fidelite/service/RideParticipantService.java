package tn.esprit.autoloc.fidelite.service;

import tn.esprit.autoloc.fidelite.entity.RideParticipant;
import tn.esprit.autoloc.fidelite.repository.RideParticipantRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class RideParticipantService {
    private final RideParticipantRepository repository;

    public RideParticipantService(RideParticipantRepository repository) {
        this.repository = repository;
    }

    public List<RideParticipant> findAll() { return repository.findAll(); }

    public RideParticipant findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant introuvable : " + id));
    }

    public List<RideParticipant> findByRide(String rideId) { return repository.findByRideId(rideId); }

    public List<RideParticipant> findByUser(String userId) { return repository.findByUserId(userId); }

    public RideParticipant create(RideParticipant p) { return repository.save(p); }

    public void delete(UUID id) { repository.deleteById(id); }
}
