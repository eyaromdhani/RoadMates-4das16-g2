package tn.esprit.autoloc.avis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.avis.entity.Review;
import tn.esprit.autoloc.avis.entity.ReviewRight;
import tn.esprit.autoloc.avis.entity.RightStatus;
import tn.esprit.autoloc.avis.repository.ReviewRepository;
import tn.esprit.autoloc.avis.repository.ReviewRightRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewRightRepository reviewRightRepository;
    private final TrustScoreService trustScoreService;

    // CREATE
    public Review create(Review review) {
        // 1. Vérifier que le droit d'avis existe ("autorise")
        ReviewRight right = reviewRightRepository
                .findByRideIdAndReviewerId(review.getRideId(), review.getReviewerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Aucun droit de laisser un avis pour ce trajet"));

        // 2. Vérifier le statut et l'expiration
        if (right.getStatus() != RightStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Le droit d'avis est déjà utilisé ou expiré");
        }
        if (right.getExpiresAt() != null && right.getExpiresAt().before(new Date())) {
            right.setStatus(RightStatus.EXPIRED);
            reviewRightRepository.save(right);
            throw new ResponseStatusException(HttpStatus.GONE, "Le droit d'avis a expiré");
        }

        // 3. Enregistrer l'avis
        review.setId(null);
        review.setRevieweeId(right.getRevieweeId());
        review.setCreatedAt(new Date());
        Review saved = reviewRepository.save(review);

        // 4. Marquer le droit comme utilisé
        right.setStatus(RightStatus.USED);
        reviewRightRepository.save(right);

        // 5. Mettre à jour le TrustScore ("met à jour")
        trustScoreService.recalculate(saved.getRevieweeId());

        return saved;
    }

    // READ
    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public Review getById(String id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Review introuvable : " + id));
    }

    public List<Review> getByReviewee(String revieweeId) {
        return reviewRepository.findByRevieweeId(revieweeId);
    }

    public List<Review> getByReviewer(String reviewerId) {
        return reviewRepository.findByReviewerId(reviewerId);
    }

    public List<Review> getByRide(String rideId) {
        return reviewRepository.findByRideId(rideId);
    }

    // UPDATE (seuls la note et le commentaire sont modifiables)
    public Review update(String id, Review updated) {
        Review existing = getById(id);
        existing.setRating(updated.getRating());
        existing.setComment(updated.getComment());
        Review saved = reviewRepository.save(existing);

        trustScoreService.recalculate(saved.getRevieweeId());
        return saved;
    }

    // DELETE
    public void delete(String id) {
        Review existing = getById(id);
        reviewRepository.deleteById(id);
        trustScoreService.recalculate(existing.getRevieweeId());
    }
}