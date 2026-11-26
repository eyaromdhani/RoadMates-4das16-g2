package tn.esprit.autoloc.paiement.service;

import tn.esprit.autoloc.paiement.dto.CreatePaymentRequest;
import tn.esprit.autoloc.paiement.dto.UpdatePaymentRequest;
import tn.esprit.autoloc.paiement.entity.Payment;
import tn.esprit.autoloc.paiement.entity.PaymentStatus;
import tn.esprit.autoloc.paiement.entity.Wallet;
import tn.esprit.autoloc.paiement.entity.WalletTransaction;
import tn.esprit.autoloc.paiement.entity.WalletTransactionType;
import tn.esprit.autoloc.paiement.repository.PaymentRepository;
import tn.esprit.autoloc.paiement.repository.WalletRepository;
import tn.esprit.autoloc.paiement.repository.WalletTransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    private final WalletRepository wallets;
    private final PaymentRepository payments;
    private final WalletTransactionRepository transactions;

    public PaymentService(WalletRepository wallets, PaymentRepository payments,
                          WalletTransactionRepository transactions) {
        this.wallets = wallets;
        this.payments = payments;
        this.transactions = transactions;
    }

    @Transactional
    public Payment pay(String ownerId, CreatePaymentRequest request) {
        Wallet wallet = wallets.findByOwnerIdForUpdate(ownerId.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
        if (wallet.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Wallet balance is too low for this payment");
        }
        wallet.setBalance(wallet.getBalance().subtract(request.amount()).setScale(2));
        Payment payment = payments.save(new Payment(wallet, request.recipientId().trim(), request.amount().setScale(2),
                normalizeDescription(request.description())));
        transactions.save(new WalletTransaction(wallet, payment, WalletTransactionType.PAYMENT,
                request.amount().setScale(2), request.description() == null ? "Payment" : request.description()));
        return payment;
    }

    @Transactional(readOnly = true)
    public List<Payment> history(String ownerId) {
        return payments.findByWalletOwnerIdOrderByCreatedAtDesc(ownerId.trim());
    }

    @Transactional(readOnly = true)
    public Payment get(String ownerId, UUID paymentId) {
        return payments.findByIdAndWalletOwnerId(paymentId, ownerId.trim())
                .orElseThrow(() -> paymentNotFound());
    }

    @Transactional
    public Payment update(String ownerId, UUID paymentId, UpdatePaymentRequest request) {
        Payment payment = findForUpdate(ownerId, paymentId);
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A refunded payment cannot be updated");
        }
        payment.updateDetails(request.recipientId().trim(), normalizeDescription(request.description()));
        return payment;
    }

    /** DELETE is an audited refund: it restores the wallet and keeps the payment record. */
    @Transactional
    public Payment refund(String ownerId, UUID paymentId) {
        Payment payment = findForUpdate(ownerId, paymentId);
        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            return payment;
        }

        Wallet wallet = wallets.findByOwnerIdForUpdate(ownerId.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
        wallet.setBalance(wallet.getBalance().add(payment.getAmount()).setScale(2));
        payment.markRefunded();
        transactions.save(new WalletTransaction(wallet, null, WalletTransactionType.REFUND,
                payment.getAmount(), "Refund for payment " + payment.getId()));
        return payment;
    }

    private Payment findForUpdate(String ownerId, UUID paymentId) {
        return payments.findByIdAndWalletOwnerIdForUpdate(paymentId, ownerId.trim())
                .orElseThrow(() -> paymentNotFound());
    }

    private ResponseStatusException paymentNotFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
    }

    private String normalizeDescription(String description) {
        return description == null || description.isBlank() ? null : description.trim();
    }
}
