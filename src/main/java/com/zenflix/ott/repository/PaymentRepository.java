package com.zenflix.ott.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zenflix.ott.entity.Payment;
import com.zenflix.ott.entity.Subscription;
import com.zenflix.ott.entity.User;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    Payment findFirstByUserAndSubscriptionOrderByTransactionDateDesc(User user, Subscription subscription);
}
