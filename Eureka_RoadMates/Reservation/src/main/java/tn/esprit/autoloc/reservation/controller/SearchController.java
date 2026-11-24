package tn.esprit.autoloc.reservation.controller;

import tn.esprit.autoloc.reservation.entity.IndexTrajet;
import tn.esprit.autoloc.reservation.service.IndexTrajetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/search/rides")
public class SearchController {
    private final IndexTrajetService service;

    public SearchController(IndexTrajetService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    public String hello() { return "Hello from Recherche & Reservation"; }

    @GetMapping
    public List<IndexTrajet> all() { return service.findAll(); }

    // GET /search/rides/find?departure=Tunis&arrival=Sfax&seats=2
    @GetMapping("/find")
    public List<IndexTrajet> find(@RequestParam String departure,
                                  @RequestParam String arrival,
                                  @RequestParam(defaultValue = "1") int seats) {
        return service.search(departure, arrival, seats);
    }

    @GetMapping("/{id}")
    public IndexTrajet one(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IndexTrajet create(@RequestBody IndexTrajet t) { return service.create(t); }

    @PutMapping("/{id}")
    public IndexTrajet update(@PathVariable Long id, @RequestBody IndexTrajet t) {
        return service.update(id, t);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}