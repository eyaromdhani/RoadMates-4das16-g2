package tn.esprit.autoloc.paiement.controller;

import tn.esprit.autoloc.paiement.dto.AmountRequest;
import tn.esprit.autoloc.paiement.dto.CreateWalletRequest;
import tn.esprit.autoloc.paiement.dto.WalletResponse;
import tn.esprit.autoloc.paiement.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService service;
    public WalletController(WalletService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse create(@Valid @RequestBody CreateWalletRequest request) {
        return WalletResponse.from(service.create(request));
    }

    @GetMapping("/me")
    public WalletResponse me(@RequestHeader("X-User-Id") String ownerId) {
        return WalletResponse.from(service.getByOwner(ownerId));
    }

    @PostMapping("/me/deposit")
    public WalletResponse deposit(@RequestHeader("X-User-Id") String ownerId,
                                  @Valid @RequestBody AmountRequest request) {
        return WalletResponse.from(service.deposit(ownerId, request));
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader("X-User-Id") String ownerId) {
        service.deleteEmptyWallet(ownerId);
    }
}
