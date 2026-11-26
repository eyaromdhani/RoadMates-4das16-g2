package tn.esprit.autoloc.trajets.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.trajets.entity.Vehicle;
import tn.esprit.autoloc.trajets.service.VehicleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rides/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle create(@RequestBody Vehicle v) {
        return service.create(v);
    }

    @GetMapping
    public List<Vehicle> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Vehicle one(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/owner/{ownerId}")
    public List<Vehicle> byOwner(@PathVariable UUID ownerId) {
        return service.findByOwner(ownerId);
    }

    @PutMapping("/{id}")
    public Vehicle update(@PathVariable Long id, @RequestBody Vehicle v) {
        return service.update(id, v);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}