package com.zenflix.ott.repository;

import com.zenflix.ott.entity.User;
import com.zenflix.ott.entity.UserSubscription;
import com.zenflix.ott.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    //  Find all active subscriptions
    List<UserSubscription> findBySubscriptionStatus(SubscriptionStatus subscriptionStatus);

    // Find queued subscriptions for a user, ordered by start date ascending
    @Query("SELECT us FROM UserSubscription us " +
            "WHERE us.user.id = :userId " +
            "  AND us.subscriptionStatus = :status " +
            "ORDER BY us.startDate ASC")
    List<UserSubscription> findQueuedByUserOrderByStartDate(Long userId, SubscriptionStatus status);

    // Find all subscriptions for the user that are either ACTIVE or QUEUED
    List<UserSubscription> findByUserAndSubscriptionStatusIn(User user, Collection<SubscriptionStatus> statuses);

    Optional<UserSubscription> findByTransactionReferenceId(String transactionReferenceId);

    boolean existsByUserAndSubscriptionStatus(User user, SubscriptionStatus subscriptionStatus);

    List<UserSubscription> findBySubscriptionStatusIn(Collection<SubscriptionStatus> statuses);

    boolean existsByUser_EmailAndSubscriptionStatus(String email, SubscriptionStatus status);

    Optional<UserSubscription> findByUserIdAndSubscriptionPlanIdAndSubscriptionStatus(
            Long userId,
            Long subscriptionPlanId,
            SubscriptionStatus subscriptionStatus
    );

    boolean existsByUser_IdAndSubscriptionStatus(Long userId, SubscriptionStatus subscriptionStatus);
}
