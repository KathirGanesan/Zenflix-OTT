package com.zenflix.ott.mapper;

import com.zenflix.ott.entity.SubscriptionPlan;
import com.zenflix.ott.entity.Transaction;
import org.springframework.stereotype.Component;

import com.zenflix.ott.dto.TransactionDTO;
import com.zenflix.ott.entity.User;
import com.zenflix.ott.enums.PaymentStatus;

@Component
public class TransactionMapper {
    public TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setUserId(transaction.getUser().getId());
        transactionDTO.setSubscriptionId(transaction.getSubscriptionPlan().getId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setPaymentStatus(transaction.getPaymentStatus().name());
        transactionDTO.setTransactionDate(transaction.getTransactionDate());
        transactionDTO.setTransactionReferenceId(transaction.getTransactionReferenceId());
        // Map auditable fields
        transactionDTO.setCreatedAt(transaction.getCreatedAt());
        transactionDTO.setCreatedBy(transaction.getCreatedBy());
        transactionDTO.setModifiedAt(transaction.getModifiedAt());
        transactionDTO.setModifiedBy(transaction.getModifiedBy());
        return transactionDTO;
    }

    public Transaction toEntity(TransactionDTO transactionDTO, User user, SubscriptionPlan subscriptionPlan) {
        if (transactionDTO == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId(transactionDTO.getId());
        transaction.setUser(user);
        transaction.setSubscriptionPlan(subscriptionPlan);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setPaymentStatus(PaymentStatus.valueOf(transactionDTO.getPaymentStatus()));
        transaction.setTransactionDate(transactionDTO.getTransactionDate());
        transaction.setTransactionReferenceId(transactionDTO.getTransactionReferenceId());
        return transaction;
    }
}
