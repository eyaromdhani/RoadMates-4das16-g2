package tn.esprit.autoloc.incidents.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.incidents.entity.Incident;
import tn.esprit.autoloc.incidents.entity.IncidentStatus;
import tn.esprit.autoloc.incidents.entity.IncidentType;
import tn.esprit.autoloc.incidents.repository.IncidentRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository repository;

    public IncidentService(IncidentRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public Incident create(Incident incident) {
        incident.setId(null);                           // l'id est toujours généré par MongoDB
        incident.setStatus(IncidentStatus.OPEN);        // un nouvel incident est toujours ouvert
        incident.setCreatedAt(LocalDateTime.now());
        incident.setUpdatedAt(LocalDateTime.now());
        return repository.save(incident);
    }

    // READ (liste, avec filtres facultatifs)
    public List<Incident> findAll(IncidentStatus status, IncidentType type) {
        if (status != null && type != null) return repository.findByStatusAndType(status, type);
        if (status != null) return repository.findByStatus(status);
        if (type != null) return repository.findByType(type);
        return repository.findAll();
    }

    // READ (un seul)
    public Incident findById(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Incident introuvable : " + id));
    }

    // UPDATE
    public Incident update(String id, Incident data) {
        Incident existing = findById(id);
        existing.setType(data.getType());
        existing.setSeverity(data.getSeverity());
        existing.setDescription(data.getDescription());
        existing.setLatitude(data.getLatitude());
        existing.setLongitude(data.getLongitude());
        existing.setTripId(data.getTripId());
        if (data.getStatus() != null) {
            existing.setStatus(data.getStatus());
        }
        existing.setUpdatedAt(LocalDateTime.now());
        return repository.save(existing);
    }

    // DELETE
    public void delete(String id) {
        findById(id);                                   // 404 si l'incident n'existe pas
        repository.deleteById(id);
    }
}