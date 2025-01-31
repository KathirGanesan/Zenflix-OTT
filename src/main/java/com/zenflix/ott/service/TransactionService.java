package com.zenflix.ott.service;

import com.zenflix.ott.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    TransactionDTO getTransactionByTransactionReferenceId(String transactionReferenceId);
    List<TransactionDTO> getAllTransactions();
    void markTransactionSuccess(String transactionReferenceId, String razorpayPaymentId);
    void markTransactionFailed(String transactionReferenceId);
}
