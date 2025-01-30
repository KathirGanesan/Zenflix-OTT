package com.zenflix.ott.service.impl;

import com.zenflix.ott.dto.TransactionDTO;
import com.zenflix.ott.entity.Transaction;
import com.zenflix.ott.enums.PaymentStatus;
import com.zenflix.ott.exception.ResourceNotFoundException;
import com.zenflix.ott.mapper.TransactionMapper;
import com.zenflix.ott.repository.TransactionRepository;
import com.zenflix.ott.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public TransactionDTO getTransactionByTransactionReferenceId(String transactionReferenceId) {
        Transaction transaction = transactionRepository.findByTransactionReferenceId(transactionReferenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found for transaction reference ID: " + transactionReferenceId));
        return transactionMapper.toDTO(transaction);
    }
    
    @Override
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void markTransactionSuccess(String transactionReferenceId, String razorpayPaymentId) {
        Transaction transaction = transactionRepository.findByTransactionReferenceId(transactionReferenceId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setPaymentStatus(PaymentStatus.SUCCESS);
        transaction.setRazorpayPaymentId(razorpayPaymentId); // Save Razorpay payment ID
        transactionRepository.save(transaction);
    }

    @Override
    public void markTransactionFailed(String transactionReferenceId) {
        Transaction transaction = transactionRepository.findByTransactionReferenceId(transactionReferenceId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setPaymentStatus(PaymentStatus.FAILED);
        transactionRepository.save(transaction);
    }
}
