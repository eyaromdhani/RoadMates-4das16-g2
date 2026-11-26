package tn.esprit.autoloc.fidelite.controller;

import tn.esprit.autoloc.fidelite.entity.RideParticipant;
import tn.esprit.autoloc.fidelite.service.RideParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loyalty/participants")
public class RideParticipantController {
    private final RideParticipantService service;

    public RideParticipantController(RideParticipantService service) {
        this.service = service;
    }

    @GetMapping
    public List<RideParticipant> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public RideParticipant one(@PathVariable UUID id) { return service.findById(id); }

    @GetMapping("/ride/{rideId}")
    public List<RideParticipant> byRide(@PathVariable String rideId) { return service.findByRide(rideId); }

    @GetMapping("/user/{userId}")
    public List<RideParticipant> byUser(@PathVariable String userId) { return service.findByUser(userId); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RideParticipant create(@RequestBody RideParticipant p) { return service.create(p); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) { service.delete(id); }
}
