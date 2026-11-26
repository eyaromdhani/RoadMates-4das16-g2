package tn.esprit.autoloc.fidelite.controller;

import tn.esprit.autoloc.fidelite.entity.*;
import tn.esprit.autoloc.fidelite.service.LoyaltyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loyalty/accounts")
public class LoyaltyController {
    private final LoyaltyService service;

    public LoyaltyController(LoyaltyService service) {
        this.service = service;
    }

    @GetMapping
    public List<LoyaltyAccount> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public LoyaltyAccount one(@PathVariable UUID id) { return service.findById(id); }

    @GetMapping("/user/{userId}")
    public LoyaltyAccount byUser(@PathVariable String userId) { return service.findByUser(userId); }

    @GetMapping("/user/{userId}/history")
    public List<PointTransaction> history(@PathVariable String userId) { return service.historique(userId); }

    @GetMapping("/user/{userId}/badges")
    public List<UserBadge> badges(@PathVariable String userId) { return service.badges(userId); }

    // POST /api/loyalty/accounts?userId=5
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoyaltyAccount create(@RequestParam String userId) { return service.create(userId); }

    // POST /api/loyalty/accounts/earn?userId=5&rideId=1&type=RIDE_COMPLETED&eventId=<uuid>
    @PostMapping("/earn")
    @ResponseStatus(HttpStatus.CREATED)
    public PointTransaction earn(@RequestParam String userId,
                                 @RequestParam String rideId,
                                 @RequestParam TransactionType type,
                                 @RequestParam(required = false) UUID eventId) {
        return service.earn(userId, rideId, type, eventId);
    }

    // POST /api/loyalty/accounts/redeem?userId=5&rewardId=<uuid>
    @PostMapping("/redeem")
    @ResponseStatus(HttpStatus.CREATED)
    public PointTransaction redeem(@RequestParam String userId, @RequestParam UUID rewardId) {
        return service.redeem(userId, rewardId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) { service.delete(id); }
}
