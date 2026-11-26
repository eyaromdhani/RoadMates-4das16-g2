package tn.esprit.autoloc.paiement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePaymentRequest(
        @NotBlank @Size(max = 120) String recipientId,
        @Size(max = 500) String description) { }
