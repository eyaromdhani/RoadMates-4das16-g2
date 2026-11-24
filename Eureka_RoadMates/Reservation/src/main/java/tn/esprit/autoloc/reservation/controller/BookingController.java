package tn.esprit.autoloc.reservation.controller;

import tn.esprit.autoloc.reservation.entity.*;
import tn.esprit.autoloc.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final ReservationService service;

    public BookingController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reservation> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public Reservation one(@PathVariable Long id) { return service.findById(id); }

    @GetMapping("/passenger/{passengerId}")
    public List<Reservation> byPassenger(@PathVariable Long passengerId) {
        return service.findByPassenger(passengerId);
    }

    @GetMapping("/{id}/history")
    public List<HistoriqueStatut> history(@PathVariable Long id) {
        return service.historique(id);
    }

    // POST /bookings?rideId=1&passengerId=5&seats=2
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation create(@RequestParam Long rideId,
                              @RequestParam Long passengerId,
                              @RequestParam int seats) {
        return service.create(rideId, passengerId, seats);
    }

    // PATCH /bookings/1/status?value=CONFIRMEE
    @PatchMapping("/{id}/status")
    public Reservation status(@PathVariable Long id, @RequestParam Status value) {
        return service.changeStatus(id, value);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}