package tn.esprit.autoloc.paiement.service;

import tn.esprit.autoloc.paiement.dto.AmountRequest;
import tn.esprit.autoloc.paiement.dto.CreateWalletRequest;
import tn.esprit.autoloc.paiement.entity.Wallet;
import tn.esprit.autoloc.paiement.entity.WalletTransaction;
import tn.esprit.autoloc.paiement.entity.WalletTransactionType;
import tn.esprit.autoloc.paiement.repository.WalletRepository;
import tn.esprit.autoloc.paiement.repository.WalletTransactionRepository;
import tn.esprit.autoloc.paiement.repository.PaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;

@Service
public class WalletService {
    private final WalletRepository wallets;
    private final WalletTransactionRepository transactions;
    private final PaymentRepository payments;

    public WalletService(WalletRepository wallets, WalletTransactionRepository transactions,
                         PaymentRepository payments) {
        this.wallets = wallets;
        this.transactions = transactions;
        this.payments = payments;
    }

    @Transactional
    public Wallet create(CreateWalletRequest request) {
        String ownerId = request.ownerId().trim();
        if (wallets.findByOwnerId(ownerId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A wallet already exists for this owner");
        }
        BigDecimal initialBalance = request.initialBalance() == null ? BigDecimal.ZERO : request.initialBalance();
        Wallet wallet = wallets.save(new Wallet(ownerId, initialBalance.setScale(2)));
        if (initialBalance.signum() > 0) {
            transactions.save(new WalletTransaction(wallet, null, WalletTransactionType.CREDIT,
                    initialBalance.setScale(2), "Initial wallet balance"));
        }
        return wallet;
    }

    @Transactional(readOnly = true)
    public Wallet getByOwner(String ownerId) {
        return wallets.findByOwnerId(ownerId.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
    }

    @Transactional
    public Wallet deposit(String ownerId, AmountRequest request) {
        Wallet wallet = wallets.findByOwnerIdForUpdate(ownerId.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
        wallet.setBalance(wallet.getBalance().add(request.amount()).setScale(2));
        transactions.save(new WalletTransaction(wallet, null, WalletTransactionType.CREDIT,
                request.amount(), request.description() == null ? "Wallet deposit" : request.description()));
        return wallet;
    }

    @Transactional
    public void deleteEmptyWallet(String ownerId) {
        Wallet wallet = wallets.findByOwnerIdForUpdate(ownerId.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
        if (transactions.existsByWalletOwnerId(ownerId.trim()) || payments.existsByWalletOwnerId(ownerId.trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A wallet with financial history cannot be deleted");
        }
        wallets.delete(wallet);
    }
}
