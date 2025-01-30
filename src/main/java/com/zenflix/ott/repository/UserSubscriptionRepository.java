package com.zenflix.ott.repository;

import com.zenflix.ott.entity.SubscriptionPlan;
import com.zenflix.ott.entity.User;
import com.zenflix.ott.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    List<UserSubscription> findByIsAutoRenewTrueAndNextRenewalDateLessThanEqual(LocalDateTime currentDate);
    Boolean existsByUserAndSubscriptionPlan(User user, SubscriptionPlan subscriptionPlan);
    UserSubscription findByUserAndActiveTrue(User user);
    UserSubscription findFirstByUserAndActiveFalseOrderByStartDateAsc(User user);
    List<UserSubscription> findAllByActiveTrue();
    Boolean existsByUserIdAndActiveTrue(Long userId);
    Boolean existsByUser_EmailAndActiveTrue(String email);
    Optional<UserSubscription> findByUserIdAndSubscriptionPlanIdAndActiveTrue(Long userId, Long subscriptionPlanId);
    Optional<UserSubscription> findByTransactionReferenceId(String transactionReferenceId);

}
