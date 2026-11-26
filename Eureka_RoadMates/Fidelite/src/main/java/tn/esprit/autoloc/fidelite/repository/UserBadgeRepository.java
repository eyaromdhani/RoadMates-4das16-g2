package tn.esprit.autoloc.fidelite.repository;

import tn.esprit.autoloc.fidelite.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UserBadgeRepository extends JpaRepository<UserBadge, UUID> {
    List<UserBadge> findByAccountId(UUID accountId);
    boolean existsByAccountIdAndBadgeId(UUID accountId, UUID badgeId);
}
