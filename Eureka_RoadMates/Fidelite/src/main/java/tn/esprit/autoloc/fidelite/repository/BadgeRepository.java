package tn.esprit.autoloc.fidelite.repository;

import tn.esprit.autoloc.fidelite.entity.Badge;
import tn.esprit.autoloc.fidelite.entity.BadgeConditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BadgeRepository extends JpaRepository<Badge, UUID> {
    Optional<Badge> findByCode(String code);
    List<Badge> findByConditionType(BadgeConditionType conditionType);
}
