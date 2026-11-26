package tn.esprit.autoloc.avis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.avis.entity.ReviewRight;
import tn.esprit.autoloc.avis.entity.RightStatus;
import tn.esprit.autoloc.avis.repository.ReviewRightRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewRightService {

    private final ReviewRightRepository reviewRightRepository;

    // CREATE
    public ReviewRight create(ReviewRight right) {
        reviewRightRepository.findByRideIdAndReviewerId(right.getRideId(), right.getReviewerId())
                .ifPresent(r -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Un droit existe déjà pour ce trajet et cet utilisateur");
                });

        right.setId(null);
        right.setStatus(RightStatus.PENDING);
        right.setCreatedAt(new Date());
        if (right.getExpiresAt() == null) {
            // par défaut : 7 jours
            right.setExpiresAt(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));
        }
        return reviewRightRepository.save(right);
    }

    // READ
    public List<ReviewRight> getAll() {
        return reviewRightRepository.findAll();
    }

    public ReviewRight getById(String id) {
        return reviewRightRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ReviewRight introuvable : " + id));
    }

    public List<ReviewRight> getByReviewer(String reviewerId) {
        return reviewRightRepository.findByReviewerId(reviewerId);
    }

    public List<ReviewRight> getByStatus(RightStatus status) {
        return reviewRightRepository.findByStatus(status);
    }

    // UPDATE
    public ReviewRight update(String id, ReviewRight updated) {
        ReviewRight existing = getById(id);
        existing.setRideId(updated.getRideId());
        existing.setReviewerId(updated.getReviewerId());
        existing.setRevieweeId(updated.getRevieweeId());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getExpiresAt() != null) existing.setExpiresAt(updated.getExpiresAt());
        return reviewRightRepository.save(existing);
    }

    public ReviewRight updateStatus(String id, RightStatus status) {
        ReviewRight existing = getById(id);
        existing.setStatus(status);
        return reviewRightRepository.save(existing);
    }

    // DELETE
    public void delete(String id) {
        if (!reviewRightRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ReviewRight introuvable : " + id);
        }
        reviewRightRepository.deleteById(id);
    }
}