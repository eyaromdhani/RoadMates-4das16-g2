package tn.esprit.autoloc.fidelite.service;

import tn.esprit.autoloc.fidelite.entity.*;
import tn.esprit.autoloc.fidelite.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class LoyaltyService {
    private static final int POINTS_RIDE_COMPLETED = 10;
    private static final int POINTS_REVIEW_SUBMITTED = 5;
    private static final int SILVER_THRESHOLD = 100;
    private static final int GOLD_THRESHOLD = 300;

    private final LoyaltyAccountRepository repository;
    private final PointTransactionRepository transactionRepository;
    private final RewardRepository rewardRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    public LoyaltyService(LoyaltyAccountRepository repository,
                          PointTransactionRepository transactionRepository,
                          RewardRepository rewardRepository,
                          BadgeRepository badgeRepository,
                          UserBadgeRepository userBadgeRepository) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
        this.rewardRepository = rewardRepository;
        this.badgeRepository = badgeRepository;
        this.userBadgeRepository = userBadgeRepository;
    }

    public List<LoyaltyAccount> findAll() { return repository.findAll(); }

    public LoyaltyAccount findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable : " + id));
    }

    public LoyaltyAccount findByUser(String userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable pour l'utilisateur : " + userId));
    }

    public List<PointTransaction> historique(String userId) {
        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(findByUser(userId).getId());
    }

    public List<UserBadge> badges(String userId) {
        return userBadgeRepository.findByAccountId(findByUser(userId).getId());
    }

    @Transactional
    public LoyaltyAccount create(String userId) {
        if (repository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Compte déjà existant pour : " + userId);
        }
        LoyaltyAccount a = new LoyaltyAccount();
        a.setUserId(userId);
        return repository.save(a);
    }

    /** Ajoute des points (RideCompleted +10, ReviewSubmitted +5). Idempotent grâce à eventId. */
    @Transactional
    public PointTransaction earn(String userId, String rideId, TransactionType type, UUID eventId) {
        if (type == TransactionType.REDEMPTION) {
            throw new RuntimeException("Utiliser l'échange pour une REDEMPTION");
        }
        if (eventId != null) {
            var existing = transactionRepository.findByEventId(eventId);
            if (existing.isPresent()) return existing.get();   // événement déjà traité
        }
        LoyaltyAccount a = repository.findByUserId(userId).orElseGet(() -> {
            LoyaltyAccount n = new LoyaltyAccount();
            n.setUserId(userId);
            return repository.save(n);
        });

        int points = (type == TransactionType.RIDE_COMPLETED) ? POINTS_RIDE_COMPLETED : POINTS_REVIEW_SUBMITTED;
        a.setPointsBalance(a.getPointsBalance() + points);
        a.setLevel(computeLevel(a.getPointsBalance()));
        repository.save(a);

        PointTransaction t = new PointTransaction();
        t.setAccount(a);
        t.setPoints(points);
        t.setType(type);
        t.setRideId(rideId);
        t.setEventId(eventId != null ? eventId : UUID.randomUUID());
        t = transactionRepository.save(t);

        checkBadges(a);
        return t;
    }

    /** Échange = transaction REDEMPTION (points négatifs, rewardId et montant en DT). */
    @Transactional
    public PointTransaction redeem(String userId, UUID rewardId) {
        LoyaltyAccount a = findByUser(userId);
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new RuntimeException("Récompense introuvable : " + rewardId));
        if (!reward.isActive()) {
            throw new RuntimeException("Récompense inactive");
        }
        if (a.getPointsBalance() < reward.getPointsCost()) {
            throw new RuntimeException("Points insuffisants");
        }
        a.setPointsBalance(a.getPointsBalance() - reward.getPointsCost());
        repository.save(a);

        PointTransaction t = new PointTransaction();
        t.setAccount(a);
        t.setPoints(-reward.getPointsCost());
        t.setType(TransactionType.REDEMPTION);
        t.setReward(reward);
        t.setCreditAmountDT(reward.getCreditAmountDT());
        t.setEventId(UUID.randomUUID());
        return transactionRepository.save(t);
    }

    @Transactional
    public void delete(UUID id) { repository.deleteById(id); }

    // BRONZE -> SILVER -> GOLD selon les points gagnés
    private Level computeLevel(int points) {
        if (points >= GOLD_THRESHOLD) return Level.GOLD;
        if (points >= SILVER_THRESHOLD) return Level.SILVER;
        return Level.BRONZE;
    }

    // Badge obtenu quand le seuil de la condition est atteint (ex. 10 trajets terminés)
    private void checkBadges(LoyaltyAccount a) {
        long rides = transactionRepository.countByAccountIdAndType(a.getId(), TransactionType.RIDE_COMPLETED);
        long reviews = transactionRepository.countByAccountIdAndType(a.getId(), TransactionType.REVIEW_SUBMITTED);

        for (Badge b : badgeRepository.findAll()) {
            long value = switch (b.getConditionType()) {
                case RIDES_COMPLETED -> rides;
                case REVIEWS_SUBMITTED -> reviews;
                case POINTS_REACHED -> a.getPointsBalance();
            };
            if (value >= b.getThreshold()
                    && !userBadgeRepository.existsByAccountIdAndBadgeId(a.getId(), b.getId())) {
                UserBadge ub = new UserBadge();
                ub.setAccount(a);
                ub.setBadge(b);
                userBadgeRepository.save(ub);
            }
        }
    }
}
