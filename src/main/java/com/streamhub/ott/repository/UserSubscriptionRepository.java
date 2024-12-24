package com.streamhub.ott.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.streamhub.ott.entity.Subscription;
import com.streamhub.ott.entity.User;
import com.streamhub.ott.entity.UserSubscription;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    List<UserSubscription> findByIsAutoRenewTrueAndNextRenewalDateBefore(LocalDateTime currentDate);
    boolean existsByUserAndSubscription(User user, Subscription subscription);
    UserSubscription findByUserAndActiveTrue(User user);
    UserSubscription findFirstByUserAndActiveFalseOrderByStartDateAsc(User user);
    List<UserSubscription> findAllByActiveTrue();
    boolean existsByUserIdAndActiveTrue(Long userId);

}
