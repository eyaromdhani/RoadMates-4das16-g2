package tn.esprit.autoloc.incidents.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.autoloc.incidents.entity.Incident;
import tn.esprit.autoloc.incidents.entity.IncidentStatus;
import tn.esprit.autoloc.incidents.entity.IncidentType;
import java.util.List;

@Repository
public interface IncidentRepository extends MongoRepository<Incident, String> {
    List<Incident> findByStatus(IncidentStatus status);
    List<Incident> findByType(IncidentType type);
    List<Incident> findByStatusAndType(IncidentStatus status, IncidentType type);
    List<Incident> findByTripId(Long tripId);
}