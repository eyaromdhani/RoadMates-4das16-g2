package tn.esprit.autoloc.paiement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CreateWalletRequest(
        @NotBlank @Size(max = 120) String ownerId,
        @DecimalMin(value = "0.00") @Digits(integer = 10, fraction = 2) BigDecimal initialBalance) { }
