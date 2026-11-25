package tn.esprit.autoloc.fidelite.service;

import tn.esprit.autoloc.fidelite.entity.Badge;
import tn.esprit.autoloc.fidelite.repository.BadgeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class BadgeService {
    private final BadgeRepository repository;

    public BadgeService(BadgeRepository repository) {
        this.repository = repository;
    }

    public List<Badge> findAll() { return repository.findAll(); }

    public Badge findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge introuvable : " + id));
    }

    public Badge create(Badge b) { return repository.save(b); }

    public Badge update(UUID id, Badge data) {
        Badge b = findById(id);
        b.setCode(data.getCode());
        b.setName(data.getName());
        b.setDescription(data.getDescription());
        b.setConditionType(data.getConditionType());
        b.setThreshold(data.getThreshold());
        return repository.save(b);
    }

    public void delete(UUID id) { repository.deleteById(id); }
}
