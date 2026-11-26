package tn.esprit.autoloc.paiement.controller;

import tn.esprit.autoloc.paiement.dto.WalletTransactionResponse;
import tn.esprit.autoloc.paiement.repository.WalletTransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class WalletTransactionController {
    private final WalletTransactionRepository transactions;
    public WalletTransactionController(WalletTransactionRepository transactions) { this.transactions = transactions; }

    @GetMapping
    public List<WalletTransactionResponse> history(@RequestHeader("X-User-Id") String ownerId) {
        return transactions.findByWalletOwnerIdOrderByCreatedAtDesc(ownerId.trim())
                .stream().map(WalletTransactionResponse::from).toList();
    }
}
