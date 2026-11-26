package tn.esprit.autoloc.avis.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.autoloc.avis.entity.ReviewRight;
import tn.esprit.autoloc.avis.entity.RightStatus;

import java.util.List;
import java.util.Optional;

public interface ReviewRightRepository extends MongoRepository<ReviewRight, String> {
    Optional<ReviewRight> findByRideIdAndReviewerId(String rideId, String reviewerId);
    List<ReviewRight> findByReviewerId(String reviewerId);
    List<ReviewRight> findByStatus(RightStatus status);
}