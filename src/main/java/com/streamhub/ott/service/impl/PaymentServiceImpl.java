package com.streamhub.ott.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streamhub.ott.dto.PaymentDTO;
import com.streamhub.ott.entity.Payment;
import com.streamhub.ott.exception.ResourceNotFoundException;
import com.streamhub.ott.mapper.PaymentMapper;
import com.streamhub.ott.repository.PaymentRepository;
import com.streamhub.ott.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public PaymentDTO getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for transaction ID: " + transactionId));
        return paymentMapper.toDTO(payment);
    }
    
    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
}
