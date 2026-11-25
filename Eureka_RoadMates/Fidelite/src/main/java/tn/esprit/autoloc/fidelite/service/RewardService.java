package tn.esprit.autoloc.fidelite.service;

import tn.esprit.autoloc.fidelite.entity.Reward;
import tn.esprit.autoloc.fidelite.repository.RewardRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class RewardService {
    private final RewardRepository repository;

    public RewardService(RewardRepository repository) {
        this.repository = repository;
    }

    public List<Reward> findAll() { return repository.findAll(); }

    public List<Reward> findActive() { return repository.findByActiveTrue(); }

    public Reward findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Récompense introuvable : " + id));
    }

    public Reward create(Reward r) { return repository.save(r); }

    public Reward update(UUID id, Reward data) {
        Reward r = findById(id);
        r.setName(data.getName());
        r.setPointsCost(data.getPointsCost());
        r.setCreditAmountDT(data.getCreditAmountDT());
        r.setActive(data.isActive());
        return repository.save(r);
    }

    public void delete(UUID id) { repository.deleteById(id); }
}
