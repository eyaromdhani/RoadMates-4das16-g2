package tn.esprit.autoloc.avis.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.autoloc.avis.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByRevieweeId(String revieweeId);
    List<Review> findByReviewerId(String reviewerId);
    List<Review> findByRideId(String rideId);
    Optional<Review> findByRideIdAndReviewerId(String rideId, String reviewerId);
}