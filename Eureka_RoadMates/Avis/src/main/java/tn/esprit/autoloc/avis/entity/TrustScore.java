package tn.esprit.autoloc.avis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "trust_scores")
public class TrustScore {

    @Id
    private String userId;
    private double averageRating;
    private int reviewCount;
    private double score;
    private Date updatedAt;
}