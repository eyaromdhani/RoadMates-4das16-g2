package tn.esprit.autoloc.fidelite.controller;

import tn.esprit.autoloc.fidelite.entity.Badge;
import tn.esprit.autoloc.fidelite.service.BadgeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loyalty/badges")
public class BadgeController {
    private final BadgeService service;

    public BadgeController(BadgeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Badge> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public Badge one(@PathVariable UUID id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Badge create(@RequestBody Badge b) { return service.create(b); }

    @PutMapping("/{id}")
    public Badge update(@PathVariable UUID id, @RequestBody Badge b) { return service.update(id, b); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) { service.delete(id); }
}
