package com.streamhub.ott.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.streamhub.ott.entity.Payment;
import com.streamhub.ott.entity.Subscription;
import com.streamhub.ott.entity.User;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    Payment findFirstByUserAndSubscriptionOrderByTransactionDateDesc(User user, Subscription subscription);
}
