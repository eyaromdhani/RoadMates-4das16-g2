package tn.esprit.autoloc.avis.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    @NotBlank
    private String rideId;

    @NotBlank
    private String reviewerId;

    @NotBlank
    private String revieweeId;

    @Min(1)
    @Max(5)
    private int rating;

    private String comment;
    private Date createdAt;
}