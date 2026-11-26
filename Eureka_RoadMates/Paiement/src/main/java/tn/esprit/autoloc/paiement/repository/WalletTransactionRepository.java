package tn.esprit.autoloc.paiement.repository;

import tn.esprit.autoloc.paiement.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {
    List<WalletTransaction> findByWalletOwnerIdOrderByCreatedAtDesc(String ownerId);
    boolean existsByWalletOwnerId(String ownerId);
}
