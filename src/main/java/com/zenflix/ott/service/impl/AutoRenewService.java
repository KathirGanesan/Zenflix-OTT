package com.zenflix.ott.service.impl;

import com.zenflix.ott.repository.TransactionRepository;
import com.zenflix.ott.repository.UserSubscriptionRepository;
import com.zenflix.ott.service.RazorpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class AutoRenewService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final TransactionRepository transactionRepository;
    private final RazorpayService razorpayService;

    @Autowired
    public AutoRenewService(UserSubscriptionRepository userSubscriptionRepository,
                            TransactionRepository transactionRepository,
                            RazorpayService razorpayService) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.transactionRepository = transactionRepository;
        this.razorpayService = razorpayService;
    }

//    @Scheduled(cron = "0 0 * * * ?") // Runs every hour
//    @Transactional
//    public void processAutoRenewals() {
//        LocalDateTime currentTime = LocalDateTime.now();
//        List<UserSubscription> subscriptionsToRenew = userSubscriptionRepository
//                .findByIsAutoRenewTrueAndNextRenewalDateLessThanEqual(currentTime);
//
//        for (UserSubscription currentSubscription : subscriptionsToRenew) {
//            try {
//                System.out.println("Processing auto-renew for subscription ID: " + currentSubscription.getId());
//
//                // Step 1: Deactivate the current subscription (temporary until payment is verified)
//                currentSubscription.setActive(false);
//                userSubscriptionRepository.save(currentSubscription);
//
//                // Step 2: Charge the user automatically via Razorpay
//                String paymentId = razorpayService.chargeAutoRenewal(currentSubscription);
//
//                // Step 3: Create a pending transaction entry
//                Transaction transaction = new Transaction();
//                transaction.setUser(currentSubscription.getUser());
//                transaction.setSubscriptionPlan(currentSubscription.getSubscriptionPlan());
//                transaction.setAmount(currentSubscription.getSubscriptionPlan().getPrice());
//                transaction.setPaymentStatus(PaymentStatus.PENDING);
//                transaction.setTransactionDate(currentTime);
//                transaction.setTransactionReferenceId(paymentId);
//                transactionRepository.save(transaction);
//
//                // Step 4: Link the transaction with the subscription for tracking
//                currentSubscription.setTransaction(transaction);
//                userSubscriptionRepository.save(currentSubscription);
//
//                System.out.println("Auto-renew initiated for subscription ID: " + currentSubscription.getId());
//                System.out.println("Awaiting webhook confirmation for payment...");
//
//            } catch (Exception e) {
//                System.err.println("Failed to process auto-renew for subscription ID: " + currentSubscription.getId());
//                e.printStackTrace();
//            }
//        }
//    }
}
