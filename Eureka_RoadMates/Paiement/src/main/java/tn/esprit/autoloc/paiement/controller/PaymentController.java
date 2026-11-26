package tn.esprit.autoloc.paiement.controller;

import tn.esprit.autoloc.paiement.dto.CreatePaymentRequest;
import tn.esprit.autoloc.paiement.dto.PaymentResponse;
import tn.esprit.autoloc.paiement.dto.UpdatePaymentRequest;
import tn.esprit.autoloc.paiement.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService service;
    public PaymentController(PaymentService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse pay(@RequestHeader("X-User-Id") String ownerId,
                               @Valid @RequestBody CreatePaymentRequest request) {
        return PaymentResponse.from(service.pay(ownerId, request));
    }

    @GetMapping
    public List<PaymentResponse> history(@RequestHeader("X-User-Id") String ownerId) {
        return service.history(ownerId).stream().map(PaymentResponse::from).toList();
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse get(@RequestHeader("X-User-Id") String ownerId,
                               @PathVariable UUID paymentId) {
        return PaymentResponse.from(service.get(ownerId, paymentId));
    }

    @PutMapping("/{paymentId}")
    public PaymentResponse update(@RequestHeader("X-User-Id") String ownerId,
                                  @PathVariable UUID paymentId,
                                  @Valid @RequestBody UpdatePaymentRequest request) {
        return PaymentResponse.from(service.update(ownerId, paymentId, request));
    }

    @DeleteMapping("/{paymentId}")
    public PaymentResponse refund(@RequestHeader("X-User-Id") String ownerId,
                                  @PathVariable UUID paymentId) {
        return PaymentResponse.from(service.refund(ownerId, paymentId));
    }
}
