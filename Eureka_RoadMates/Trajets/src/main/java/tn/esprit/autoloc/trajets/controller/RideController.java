package tn.esprit.autoloc.trajets.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.trajets.entity.Ride;
import tn.esprit.autoloc.trajets.service.RideService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ride create(@RequestBody Ride ride) {
        return service.create(ride);
    }

    // GET /api/rides  ou  GET /api/rides?from=Tunis&to=Sfax
    @GetMapping
    public List<Ride> search(@RequestParam(required = false) String from,
                             @RequestParam(required = false) String to) {
        return service.search(from, to);
    }

    @GetMapping("/{id}")
    public Ride one(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/driver/{driverId}")
    public List<Ride> byDriver(@PathVariable UUID driverId) {
        return service.findByDriver(driverId);
    }

    @PutMapping("/{id}")
    public Ride update(@PathVariable Long id, @RequestBody Ride ride) {
        return service.update(id, ride);
    }

    // PATCH /api/rides/1/status?value=CANCELLED
    @PatchMapping("/{id}/status")
    public Ride updateStatus(@PathVariable Long id, @RequestParam("value") String status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}