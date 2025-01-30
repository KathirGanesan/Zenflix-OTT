package com.zenflix.ott.controller;

import com.zenflix.ott.dto.TransactionDTO;
import com.zenflix.ott.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Get transaction details by transaction reference ID
    @GetMapping("/transaction/{transactionReferenceId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Both USER and ADMIN can view transaction details
    public ResponseEntity<TransactionDTO> getTransactionByTransactionReferenceId(@PathVariable String transactionReferenceId) {
        TransactionDTO transaction = transactionService.getTransactionByTransactionReferenceId(transactionReferenceId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can view all transactions
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}

