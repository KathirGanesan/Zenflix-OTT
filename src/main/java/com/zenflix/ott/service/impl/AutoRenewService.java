package com.zenflix.ott.service.impl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zenflix.ott.entity.Payment;
import com.zenflix.ott.entity.UserSubscription;
import com.zenflix.ott.enums.PaymentStatus;
import com.zenflix.ott.repository.PaymentRepository;
import com.zenflix.ott.repository.UserSubscriptionRepository;

@Service
public class AutoRenewService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final PaymentRepository paymentRepository;

    public AutoRenewService(UserSubscriptionRepository userSubscriptionRepository, PaymentRepository paymentRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.paymentRepository = paymentRepository;
    }

    @Scheduled(cron = "0 * * * * ?") // Run every minute
    @Transactional
    public void processAutoRenewals() {
        LocalDateTime currentTime = LocalDateTime.now();
//        System.out.println("Current Time for Auto-Renew Check: " + currentTime);
        
        // Fetch subscriptions eligible for auto-renew
        List<UserSubscription> subscriptionsToRenew = userSubscriptionRepository
            .findByIsAutoRenewTrueAndNextRenewalDateLessThanEqual(currentTime);

        for (UserSubscription currentSubscription : subscriptionsToRenew) {
            try {
                // Log subscription details
                System.out.println("Processing auto-renew for subscription ID: " + currentSubscription.getId());

                // Check and activate the next queued subscription (if any)
                UserSubscription nextSubscription = userSubscriptionRepository
                    .findFirstByUserAndActiveFalseOrderByStartDateAsc(currentSubscription.getUser());
                if (nextSubscription != null && nextSubscription.getStartDate().isBefore(currentTime)) {
                    // Activate queued subscription
                    nextSubscription.setActive(true);
                    userSubscriptionRepository.save(nextSubscription);

                    // Mark current subscription as inactive
                    currentSubscription.setActive(false);
                    userSubscriptionRepository.save(currentSubscription);
                    System.out.println("Activated queued subscription ID: " + nextSubscription.getId());
                    continue; // Skip payment and renewal logic for queued subscriptions
                }

                // Mark current subscription as inactive
                currentSubscription.setActive(false);
                userSubscriptionRepository.save(currentSubscription);

                // Process payment for renewal
                Payment payment = new Payment();
                payment.setUser(currentSubscription.getUser());
                payment.setSubscription(currentSubscription.getSubscription());
                payment.setAmount(currentSubscription.getSubscription().getPrice());
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                payment.setTransactionDate(currentTime);
                payment.setTransactionId(UUID.randomUUID().toString());
                paymentRepository.save(payment);

                // Update subscription dates for renewal
                currentSubscription.setStartDate(currentSubscription.getNextRenewalDate());
                currentSubscription.setEndDate(currentSubscription.getNextRenewalDate().plusMonths(currentSubscription.getSubscription().getDurationMonths()));
                currentSubscription.setNextRenewalDate(currentSubscription.getEndDate()); // Align next renewal date with end date
                currentSubscription.setActive(true); // Mark as active for renewed period
                
                
                userSubscriptionRepository.save(currentSubscription);

                System.out.println("Auto-renew successful for subscription ID: " + currentSubscription.getId());
            } catch (Exception e) {
                // Handle errors
                System.err.println("Failed to process auto-renew for subscription ID: " + currentSubscription.getId());
                e.printStackTrace();
            }
        }
    }


}
