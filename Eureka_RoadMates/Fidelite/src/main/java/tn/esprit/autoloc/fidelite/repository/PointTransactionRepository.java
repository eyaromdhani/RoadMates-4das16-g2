package tn.esprit.autoloc.fidelite.repository;

import tn.esprit.autoloc.fidelite.entity.PointTransaction;
import tn.esprit.autoloc.fidelite.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, UUID> {
    List<PointTransaction> findByAccountIdOrderByCreatedAtDesc(UUID accountId);
    Optional<PointTransaction> findByEventId(UUID eventId);
    long countByAccountIdAndType(UUID accountId, TransactionType type);
}
