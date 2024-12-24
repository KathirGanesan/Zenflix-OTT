package com.streamhub.ott.service;

import java.util.List;

import com.streamhub.ott.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO getPaymentByTransactionId(String transactionId);
    List<PaymentDTO> getAllPayments();
}
