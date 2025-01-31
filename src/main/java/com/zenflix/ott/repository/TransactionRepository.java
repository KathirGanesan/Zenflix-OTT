package com.zenflix.ott.repository;

import com.zenflix.ott.entity.SubscriptionPlan;
import com.zenflix.ott.entity.Transaction;
import com.zenflix.ott.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionReferenceId(String transactionReferenceId);
    Transaction findFirstByUserAndSubscriptionPlanOrderByTransactionDateDesc(User user, SubscriptionPlan subscriptionPlan);
}
