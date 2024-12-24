package com.streamhub.ott.service.impl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamhub.ott.entity.Payment;
import com.streamhub.ott.entity.UserSubscription;
import com.streamhub.ott.enums.PaymentStatus;
import com.streamhub.ott.repository.PaymentRepository;
import com.streamhub.ott.repository.UserSubscriptionRepository;

@Service
public class AutoRenewService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final PaymentRepository paymentRepository;

    public AutoRenewService(UserSubscriptionRepository userSubscriptionRepository, PaymentRepository paymentRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.paymentRepository = paymentRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    @Transactional
    public void processAutoRenewals() {
        // Fetch subscriptions eligible for auto-renew
        List<UserSubscription> subscriptionsToRenew = userSubscriptionRepository.findByIsAutoRenewTrueAndNextRenewalDateBefore(LocalDateTime.now());

        for (UserSubscription currentSubscription : subscriptionsToRenew) {
            try {
                // Mark current subscription as inactive
                currentSubscription.setActive(false);
                userSubscriptionRepository.save(currentSubscription);

                // Activate the next queued subscription (if any)
                UserSubscription nextSubscription = userSubscriptionRepository.findFirstByUserAndActiveFalseOrderByStartDateAsc(currentSubscription.getUser());
                if (nextSubscription != null && nextSubscription.getStartDate().isBefore(LocalDateTime.now())) {
                    nextSubscription.setActive(true);
                    userSubscriptionRepository.save(nextSubscription);
                    continue; // Skip payment and renewal logic for queued subscriptions
                }

                // Process payment for the renewed subscription
                Payment payment = new Payment();
                payment.setUser(currentSubscription.getUser());
                payment.setSubscription(currentSubscription.getSubscription());
                payment.setAmount(currentSubscription.getSubscription().getPrice());
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                payment.setTransactionDate(LocalDateTime.now());
                payment.setTransactionId(UUID.randomUUID().toString());
                paymentRepository.save(payment);

                // Update subscription dates for the renewed entry
                currentSubscription.setStartDate(currentSubscription.getNextRenewalDate());
                currentSubscription.setEndDate(currentSubscription.getNextRenewalDate().plusMonths(currentSubscription.getSubscription().getDurationMonths()));
                currentSubscription.setNextRenewalDate(currentSubscription.getEndDate().plusDays(1));
                currentSubscription.setActive(true); // Mark as active for renewed period
                userSubscriptionRepository.save(currentSubscription);

            } catch (Exception e) {
                // Handle errors
                System.err.println("Failed to process auto-renew for subscription ID: " + currentSubscription.getId());
            }
        }
    }


}
