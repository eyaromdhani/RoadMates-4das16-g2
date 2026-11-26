package tn.esprit.autoloc.paiement.repository;

import tn.esprit.autoloc.paiement.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByWalletOwnerIdOrderByCreatedAtDesc(String ownerId);
    Optional<Payment> findByIdAndWalletOwnerId(UUID id, String ownerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Payment p join p.wallet w where p.id = :paymentId and w.ownerId = :ownerId")
    Optional<Payment> findByIdAndWalletOwnerIdForUpdate(@Param("paymentId") UUID paymentId,
                                                        @Param("ownerId") String ownerId);

    boolean existsByWalletOwnerId(String ownerId);
}
