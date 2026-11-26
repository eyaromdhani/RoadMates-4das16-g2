package tn.esprit.autoloc.avis.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.autoloc.avis.entity.TrustScore;

public interface TrustScoreRepository extends MongoRepository<TrustScore, String> {
}