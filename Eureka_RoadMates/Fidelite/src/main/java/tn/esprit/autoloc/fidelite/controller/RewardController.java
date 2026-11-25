package tn.esprit.autoloc.fidelite.controller;

import tn.esprit.autoloc.fidelite.entity.Reward;
import tn.esprit.autoloc.fidelite.service.RewardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loyalty/rewards")
public class RewardController {
    private final RewardService service;

    public RewardController(RewardService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reward> all() { return service.findAll(); }

    @GetMapping("/active")
    public List<Reward> active() { return service.findActive(); }

    @GetMapping("/{id}")
    public Reward one(@PathVariable UUID id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reward create(@RequestBody Reward r) { return service.create(r); }

    @PutMapping("/{id}")
    public Reward update(@PathVariable UUID id, @RequestBody Reward r) { return service.update(id, r); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) { service.delete(id); }
}
