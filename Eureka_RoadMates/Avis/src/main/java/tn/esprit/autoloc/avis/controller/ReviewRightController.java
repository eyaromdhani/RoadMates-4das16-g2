package tn.esprit.autoloc.avis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.avis.entity.ReviewRight;
import tn.esprit.autoloc.avis.entity.RightStatus;
import tn.esprit.autoloc.avis.service.ReviewRightService;

import java.util.List;

@RestController
@RequestMapping("/review-rights")
@RequiredArgsConstructor
public class ReviewRightController {

    private final ReviewRightService reviewRightService;

    @PostMapping
    public ResponseEntity<ReviewRight> create(@RequestBody ReviewRight right) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewRightService.create(right));
    }

    @GetMapping
    public List<ReviewRight> getAll() {
        return reviewRightService.getAll();
    }

    @GetMapping("/{id}")
    public ReviewRight getById(@PathVariable String id) {
        return reviewRightService.getById(id);
    }

    @GetMapping("/reviewer/{reviewerId}")
    public List<ReviewRight> getByReviewer(@PathVariable String reviewerId) {
        return reviewRightService.getByReviewer(reviewerId);
    }

    @GetMapping("/status/{status}")
    public List<ReviewRight> getByStatus(@PathVariable RightStatus status) {
        return reviewRightService.getByStatus(status);
    }

    @PutMapping("/{id}")
    public ReviewRight update(@PathVariable String id, @RequestBody ReviewRight right) {
        return reviewRightService.update(id, right);
    }

    @PatchMapping("/{id}/status")
    public ReviewRight updateStatus(@PathVariable String id, @RequestParam RightStatus status) {
        return reviewRightService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        reviewRightService.delete(id);
        return ResponseEntity.noContent().build();
    }
}