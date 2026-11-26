package tn.esprit.autoloc.incidents.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.incidents.entity.Incident;
import tn.esprit.autoloc.incidents.entity.IncidentStatus;
import tn.esprit.autoloc.incidents.entity.IncidentType;
import tn.esprit.autoloc.incidents.service.IncidentService;
import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService service;

    public IncidentController(IncidentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Incident create(@Valid @RequestBody Incident incident) {
        return service.create(incident);
    }

    @GetMapping
    public List<Incident> findAll(@RequestParam(required = false) IncidentStatus status,
                                  @RequestParam(required = false) IncidentType type) {
        return service.findAll(status, type);
    }

    @GetMapping("/{id}")
    public Incident findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Incident update(@PathVariable String id, @Valid @RequestBody Incident incident) {
        return service.update(id, incident);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}