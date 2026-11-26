package tn.esprit.autoloc.avis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.avis.entity.Review;
import tn.esprit.autoloc.avis.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> create(@Valid @RequestBody Review review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(review));
    }

    @GetMapping
    public List<Review> getAll() {
        return reviewService.getAll();
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable String id) {
        return reviewService.getById(id);
    }

    @GetMapping("/reviewee/{revieweeId}")
    public List<Review> getByReviewee(@PathVariable String revieweeId) {
        return reviewService.getByReviewee(revieweeId);
    }

    @GetMapping("/reviewer/{reviewerId}")
    public List<Review> getByReviewer(@PathVariable String reviewerId) {
        return reviewService.getByReviewer(reviewerId);
    }

    @GetMapping("/ride/{rideId}")
    public List<Review> getByRide(@PathVariable String rideId) {
        return reviewService.getByRide(rideId);
    }

    @PutMapping("/{id}")
    public Review update(@PathVariable String id, @Valid @RequestBody Review review) {
        return reviewService.update(id, review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}