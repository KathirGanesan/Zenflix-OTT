package com.zenflix.ott.mapper;

import org.springframework.stereotype.Component;

import com.zenflix.ott.dto.PaymentDTO;
import com.zenflix.ott.entity.Payment;
import com.zenflix.ott.entity.Subscription;
import com.zenflix.ott.entity.User;
import com.zenflix.ott.enums.PaymentStatus;

@Component
public class PaymentMapper {
    public PaymentDTO toDTO(Payment payment) {
        if (payment == null) {
            return null;
        }
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());
        paymentDTO.setUserId(payment.getUser().getId());
        paymentDTO.setSubscriptionId(payment.getSubscription().getId());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setPaymentStatus(payment.getPaymentStatus().name());
        paymentDTO.setTransactionDate(payment.getTransactionDate());
        paymentDTO.setTransactionId(payment.getTransactionId());
        // Map auditable fields
        paymentDTO.setCreatedAt(payment.getCreatedAt());
        paymentDTO.setCreatedBy(payment.getCreatedBy());
        paymentDTO.setModifiedAt(payment.getModifiedAt());
        paymentDTO.setModifiedBy(payment.getModifiedBy());
        return paymentDTO;
    }

    public Payment toEntity(PaymentDTO paymentDTO, User user, Subscription subscription) {
        if (paymentDTO == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(paymentDTO.getId());
        payment.setUser(user);
        payment.setSubscription(subscription);
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentStatus(PaymentStatus.valueOf(paymentDTO.getPaymentStatus()));
        payment.setTransactionDate(paymentDTO.getTransactionDate());
        payment.setTransactionId(paymentDTO.getTransactionId());
        return payment;
    }
}
