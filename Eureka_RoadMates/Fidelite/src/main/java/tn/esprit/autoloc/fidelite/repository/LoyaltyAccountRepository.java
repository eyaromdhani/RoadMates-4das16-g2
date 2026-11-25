package tn.esprit.autoloc.fidelite.repository;

import tn.esprit.autoloc.fidelite.entity.LoyaltyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface LoyaltyAccountRepository extends JpaRepository<LoyaltyAccount, UUID> {
    Optional<LoyaltyAccount> findByUserId(String userId);
}
