package com.streamhub.ott.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streamhub.ott.dto.UserSubscriptionDTO;
import com.streamhub.ott.entity.Payment;
import com.streamhub.ott.entity.Subscription;
import com.streamhub.ott.entity.User;
import com.streamhub.ott.entity.UserSubscription;
import com.streamhub.ott.enums.PaymentStatus;
import com.streamhub.ott.exception.ResourceNotFoundException;
import com.streamhub.ott.exception.SubscriptionConflictException;
import com.streamhub.ott.mapper.UserSubscriptionMapper;
import com.streamhub.ott.repository.PaymentRepository;
import com.streamhub.ott.repository.SubscriptionRepository;
import com.streamhub.ott.repository.UserRepository;
import com.streamhub.ott.repository.UserSubscriptionRepository;
import com.streamhub.ott.service.UserSubscriptionService;

import jakarta.transaction.Transactional;

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserSubscriptionMapper userSubscriptionMapper;

    @Override
    @Transactional
    public UserSubscriptionDTO userSubscribe(UserSubscriptionDTO userSubscriptionDTO) {
        User user = userRepository.findById(userSubscriptionDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Subscription subscription = subscriptionRepository.findById(userSubscriptionDTO.getSubscriptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        // Check for active subscription
        UserSubscription activeSubscription = userSubscriptionRepository.findByUserAndActiveTrue(user);

        if (activeSubscription != null) {
            if (activeSubscription.getSubscription().equals(subscription) && activeSubscription.getIsAutoRenew()) {
                // If auto-renew is true and the same plan is being subscribed
                throw new SubscriptionConflictException("You are already subscribed to this plan.");
            } else {
                // Auto-renew is not enabled, queue the new entry
                return queueNewSubscription(user, subscription, activeSubscription.getEndDate().plusDays(1), userSubscriptionDTO.getIsAutoRenew());
            }
        }

        // No active subscription, create a new subscription
        return createNewSubscription(user, subscription, LocalDateTime.now(), userSubscriptionDTO.getIsAutoRenew());
    }


    private UserSubscriptionDTO createNewSubscription(User user, Subscription subscription, LocalDateTime startDate, Boolean isAutoRenew) {
        LocalDateTime endDate = startDate.plusMonths(subscription.getDurationMonths());
        LocalDateTime nextRenewalDate = endDate;

        // Create payment entry
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setSubscription(subscription);
        payment.setAmount(subscription.getPrice());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionDate(startDate);
        payment.setTransactionId(UUID.randomUUID().toString());
        paymentRepository.save(payment);

        // Create user subscription entry
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setSubscription(subscription);
        userSubscription.setStartDate(startDate);
        userSubscription.setEndDate(endDate);
        userSubscription.setNextRenewalDate(nextRenewalDate);
        userSubscription.setIsAutoRenew(isAutoRenew); // Set from user input
        userSubscription.setActive(true);

        UserSubscription savedUserSubscription = userSubscriptionRepository.save(userSubscription);

        // Return DTO
        UserSubscriptionDTO responseDTO = userSubscriptionMapper.toDTO(savedUserSubscription);
        responseDTO.setPaymentAmount(subscription.getPrice());
        responseDTO.setTransactionId(payment.getTransactionId());
        responseDTO.setPaymentStatusMessage("Payment successful with transaction ID: " + payment.getTransactionId());
        return responseDTO;
    }


    
    private UserSubscriptionDTO queueNewSubscription(User user, Subscription subscription, LocalDateTime startDate, Boolean isAutoRenew) {
        LocalDateTime endDate = startDate.plusMonths(subscription.getDurationMonths());
        LocalDateTime nextRenewalDate = endDate;

        // Create user subscription entry
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setSubscription(subscription);
        userSubscription.setStartDate(startDate);
        userSubscription.setEndDate(endDate);
        userSubscription.setNextRenewalDate(nextRenewalDate);
        userSubscription.setIsAutoRenew(isAutoRenew); // Set from user input
        userSubscription.setActive(false); // This will be inactive until the current plan ends

        UserSubscription savedUserSubscription = userSubscriptionRepository.save(userSubscription);

        // Return DTO
        return userSubscriptionMapper.toDTO(savedUserSubscription);
    }

    @Override
    public UserSubscriptionDTO getUserSubscriptionById(Long id) {
        UserSubscription userSubscription = userSubscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSubscription not found"));
        
        UserSubscriptionDTO dto = userSubscriptionMapper.toDTO(userSubscription);

        // Fetch payment details
        Payment payment = paymentRepository.findFirstByUserAndSubscriptionOrderByTransactionDateDesc(
                userSubscription.getUser(),
                userSubscription.getSubscription()
        );

        if (payment != null) {
            dto.setPaymentAmount(payment.getAmount());
            dto.setTransactionId(payment.getTransactionId());
            dto.setPaymentStatusMessage(
                    "Payment Status: " + payment.getPaymentStatus() + ", Transaction ID: " + payment.getTransactionId()
            );
        }

        return dto;
    }

    
    @Override
    public List<UserSubscriptionDTO> getAllUserSubscriptions() {
        return userSubscriptionRepository.findAllByActiveTrue().stream()
                .map(userSubscription -> {
                    UserSubscriptionDTO dto = userSubscriptionMapper.toDTO(userSubscription);

                    // Fetch payment details
                    Payment payment = paymentRepository.findFirstByUserAndSubscriptionOrderByTransactionDateDesc(
                            userSubscription.getUser(),
                            userSubscription.getSubscription()
                    );

                    if (payment != null) {
                        dto.setPaymentAmount(payment.getAmount());
                        dto.setTransactionId(payment.getTransactionId());
                        dto.setPaymentStatusMessage(
                                "Payment Status: " + payment.getPaymentStatus() + ", Transaction ID: " + payment.getTransactionId()
                        );
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public void unsubscribe(Long id) {
        UserSubscription userSubscription = userSubscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSubscription not found"));

        // Mark subscription as inactive
        userSubscription.setActive(false);
        userSubscription.setIsAutoRenew(false); // Disable auto-renew as part of unsubscribe
        userSubscriptionRepository.save(userSubscription);
    }

    @Override
    public UserSubscriptionDTO toggleAutoRenew(Long id) {
        // Fetch the UserSubscription by ID
        UserSubscription userSubscription = userSubscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSubscription not found"));

        // Toggle the isAutoRenew field
        userSubscription.setIsAutoRenew(!userSubscription.getIsAutoRenew());

        // Save the updated subscription
        UserSubscription updatedSubscription = userSubscriptionRepository.save(userSubscription);

        // Return the updated DTO
        return userSubscriptionMapper.toDTO(updatedSubscription);
    }

    public boolean isOwner(Long userSubscriptionId, Long userId) {
        return userSubscriptionRepository.findById(userSubscriptionId)
                .map(subscription -> subscription.getUser().getId().equals(userId))
                .orElse(false);
    }

}
