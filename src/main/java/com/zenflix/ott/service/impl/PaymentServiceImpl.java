package com.zenflix.ott.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zenflix.ott.dto.PaymentDTO;
import com.zenflix.ott.entity.Payment;
import com.zenflix.ott.exception.ResourceNotFoundException;
import com.zenflix.ott.mapper.PaymentMapper;
import com.zenflix.ott.repository.PaymentRepository;
import com.zenflix.ott.service.PaymentService;

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
