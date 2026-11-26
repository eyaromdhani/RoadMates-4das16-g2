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
@Document(collection = "review_rights")
public class ReviewRight {

    @Id
    private String id;
    private String rideId;
    private String reviewerId;
    private String revieweeId;
    private RightStatus status;
    private Date createdAt;
    private Date expiresAt;
}