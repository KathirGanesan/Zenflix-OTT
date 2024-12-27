package com.zenflix.ott.service;

import java.util.List;

import com.zenflix.ott.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO getPaymentByTransactionId(String transactionId);
    List<PaymentDTO> getAllPayments();
}
