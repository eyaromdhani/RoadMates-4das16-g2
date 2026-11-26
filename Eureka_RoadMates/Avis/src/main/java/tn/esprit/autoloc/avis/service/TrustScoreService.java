package tn.esprit.autoloc.avis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.avis.entity.Review;
import tn.esprit.autoloc.avis.entity.TrustScore;
import tn.esprit.autoloc.avis.repository.ReviewRepository;
import tn.esprit.autoloc.avis.repository.TrustScoreRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrustScoreService {

    private final TrustScoreRepository trustScoreRepository;
    private final ReviewRepository reviewRepository;

    // READ
    public TrustScore getByUserId(String userId) {
        return trustScoreRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "TrustScore introuvable pour l'utilisateur " + userId));
    }

    public List<TrustScore> getAll() {
        return trustScoreRepository.findAll();
    }

    // CREATE / UPDATE : recalcul à partir des avis reçus
    public TrustScore recalculate(String userId) {
        List<Review> reviews = reviewRepository.findByRevieweeId(userId);

        TrustScore ts = trustScoreRepository.findById(userId).orElseGet(() -> {
            TrustScore t = new TrustScore();
            t.setUserId(userId);
            return t;
        });

        int count = reviews.size();
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);

        ts.setReviewCount(count);
        ts.setAverageRating(Math.round(avg * 100.0) / 100.0);
        // score sur 100 : (moyenne / 5) * 100
        ts.setScore(Math.round((avg / 5.0) * 100.0 * 100.0) / 100.0);
        ts.setUpdatedAt(new Date());

        return trustScoreRepository.save(ts);
    }

    // DELETE
    public void delete(String userId) {
        if (!trustScoreRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TrustScore introuvable");
        }
        trustScoreRepository.deleteById(userId);
    }
}