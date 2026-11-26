package tn.esprit.autoloc.avis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.avis.entity.TrustScore;
import tn.esprit.autoloc.avis.service.TrustScoreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrustScoreController {

    private final TrustScoreService trustScoreService;

    @GetMapping("/users/{id}/trust-score")
    public TrustScore getByUserId(@PathVariable("id") String userId) {
        return trustScoreService.getByUserId(userId);
    }

    @GetMapping("/trust-scores")
    public List<TrustScore> getAll() {
        return trustScoreService.getAll();
    }

    // Recalcul manuel du score
    @PostMapping("/users/{id}/trust-score/recalculate")
    public TrustScore recalculate(@PathVariable("id") String userId) {
        return trustScoreService.recalculate(userId);
    }

    @DeleteMapping("/users/{id}/trust-score")
    public ResponseEntity<Void> delete(@PathVariable("id") String userId) {
        trustScoreService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}