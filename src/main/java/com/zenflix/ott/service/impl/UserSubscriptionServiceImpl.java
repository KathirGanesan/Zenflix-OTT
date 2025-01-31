package com.zenflix.ott.service.impl;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.zenflix.ott.dto.UserSubscriptionDTO;
import com.zenflix.ott.entity.SubscriptionPlan;
import com.zenflix.ott.entity.Transaction;
import com.zenflix.ott.entity.User;
import com.zenflix.ott.entity.UserSubscription;
import com.zenflix.ott.enums.PaymentStatus;
import com.zenflix.ott.enums.SubscriptionStatus;
import com.zenflix.ott.exception.ResourceNotFoundException;
import com.zenflix.ott.mapper.UserSubscriptionMapper;
import com.zenflix.ott.repository.SubscriptionPlanRepository;
import com.zenflix.ott.repository.TransactionRepository;
import com.zenflix.ott.repository.UserRepository;
import com.zenflix.ott.repository.UserSubscriptionRepository;
import com.zenflix.ott.service.RazorpayService;
import com.zenflix.ott.service.UserSubscriptionService;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final UserRepository userRepository;

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    private final TransactionRepository transactionRepository;

    private final UserSubscriptionMapper userSubscriptionMapper;

    private final RazorpayService razorpayService;

    public UserSubscriptionServiceImpl(UserSubscriptionRepository userSubscriptionRepository, UserRepository userRepository, SubscriptionPlanRepository subscriptionPlanRepository, TransactionRepository transactionRepository, UserSubscriptionMapper userSubscriptionMapper, RazorpayService razorpayService) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.userRepository = userRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.transactionRepository = transactionRepository;
        this.userSubscriptionMapper = userSubscriptionMapper;
        this.razorpayService = razorpayService;
    }

    @Override
    @Transactional
    public UserSubscriptionDTO userSubscribe(UserSubscriptionDTO userSubscriptionDTO) {
        User user = userRepository.findById(userSubscriptionDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(userSubscriptionDTO.getSubscriptionId())
                .orElseThrow(() -> new ResourceNotFoundException("SubscriptionPlan not found"));

        // 1. Fetch all ACTIVE or QUEUED subscriptions for the user
        List<UserSubscription> activeOrQueuedSubs = userSubscriptionRepository
                .findByUserAndSubscriptionStatusIn(
                        user,
                        List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.QUEUED)
                );

        // 2. Find the maximum endDate among them
        LocalDateTime fallback = LocalDateTime.now();
        LocalDateTime maxEndDate = activeOrQueuedSubs.stream()
                .map(UserSubscription::getEndDate)
                .max(LocalDateTime::compareTo)
                .orElse(fallback);

        // 3. The new subscription should start either:
        //    - immediately if no existing subscription or if the latest ended in the past
        //    - next day after the last subscription if it extends into the future
        LocalDateTime nextStartDate =
                maxEndDate.isBefore(LocalDateTime.now())
                        ? LocalDateTime.now()
                        : maxEndDate;

        // 4. Create a new subscription (initially in PENDING, because payment not done)
        return createSubscriptionWithPayment(user, subscriptionPlan, nextStartDate);
    }



    private UserSubscriptionDTO createSubscriptionWithPayment(
            User user,
            SubscriptionPlan subscriptionPlan,
            LocalDateTime startDate) {

        LocalDateTime endDate = startDate.plusMonths(subscriptionPlan.getDurationMonths());

        // 1. Generate transaction reference
        String transactionReferenceId = "txn_" + System.currentTimeMillis();

        // 2. Create Razorpay Payment Link
        String paymentUrl;
        try {
            RazorpayClient razorpayClient = razorpayService.getClient();

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", subscriptionPlan.getPrice().multiply(BigDecimal.valueOf(100)).intValue());
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("description", "Subscription payment for plan: " + subscriptionPlan.getPlanName());
            paymentLinkRequest.put("reference_id", transactionReferenceId);

            // Customer info
            JSONObject customer = new JSONObject();
            customer.put("name", user.getFirstName() + " " + user.getLastName());
            customer.put("contact", user.getPhoneNumber());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            // Notifications
            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("reminder_enable", true);

            // Create link
            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
            paymentUrl = paymentLink.get("short_url").toString();

        } catch (Exception e) {
            throw new RuntimeException("Error creating Razorpay payment link: " + e.getMessage());
        }

        // 3. Create a Pending Transaction Entry
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setSubscriptionPlan(subscriptionPlan);
        transaction.setAmount(subscriptionPlan.getPrice());
        transaction.setPaymentStatus(PaymentStatus.PENDING);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionReferenceId(transactionReferenceId);
        transactionRepository.save(transaction);

        // 4. Create a new PENDING User Subscription
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setSubscriptionPlan(subscriptionPlan);
        userSubscription.setStartDate(startDate);
        userSubscription.setEndDate(endDate);
        userSubscription.setNextRenewalDate(endDate);
        userSubscription.setTransactionReferenceId(transactionReferenceId);
        userSubscription.setSubscriptionStatus(SubscriptionStatus.PENDING);

        userSubscriptionRepository.save(userSubscription);

        // 5. Map to DTO and Return
        UserSubscriptionDTO responseDTO = userSubscriptionMapper.toDTO(userSubscription);
        responseDTO.setPaymentAmount(subscriptionPlan.getPrice());
        responseDTO.setTransactionReferenceId(transactionReferenceId);
        responseDTO.setPaymentStatusMessage("Pending payment. Complete payment using the provided link.");
        responseDTO.setPaymentLink(paymentUrl);
        return responseDTO;
    }



    @Override
    public UserSubscriptionDTO getUserSubscriptionById(Long id) {
        UserSubscription userSubscription = userSubscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSubscription not found"));
        
        UserSubscriptionDTO dto = userSubscriptionMapper.toDTO(userSubscription);

        // Fetch transaction details
        Transaction transaction = transactionRepository.findFirstByUserAndSubscriptionPlanOrderByTransactionDateDesc(
                userSubscription.getUser(),
                userSubscription.getSubscriptionPlan()
        );

        if (transaction != null) {
            dto.setPaymentAmount(transaction.getAmount());
            dto.setTransactionReferenceId(transaction.getTransactionReferenceId());
            dto.setPaymentStatusMessage(
                    "Transaction Status: " + transaction.getPaymentStatus() + ", Transaction ID: " + transaction.getTransactionReferenceId()
            );
        }

        return dto;
    }


    @Override
    public List<UserSubscriptionDTO> getActiveAndQueuedUserSubscriptions() {
        // Fetch all subscriptions that are either ACTIVE or QUEUED
        List<UserSubscription> subscriptions = userSubscriptionRepository
                .findBySubscriptionStatusIn(
                        Arrays.asList(SubscriptionStatus.ACTIVE, SubscriptionStatus.QUEUED)
                );

        return subscriptions.stream()
                .map(userSubscription -> {
                    UserSubscriptionDTO dto = userSubscriptionMapper.toDTO(userSubscription);

                    // 🔸 Fetch the most recent transaction for this user & subscription plan
                    Transaction transaction = transactionRepository
                            .findFirstByUserAndSubscriptionPlanOrderByTransactionDateDesc(
                                    userSubscription.getUser(),
                                    userSubscription.getSubscriptionPlan()
                            );

                    if (transaction != null) {
                        dto.setPaymentAmount(transaction.getAmount());
                        dto.setTransactionReferenceId(transaction.getTransactionReferenceId());
                        dto.setPaymentStatusMessage(
                                "Transaction Status: " + transaction.getPaymentStatus()
                                        + ", Transaction ID: " + transaction.getTransactionReferenceId()
                        );
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }



    @Override
    @Transactional
    public void unsubscribe(Long userId, Long userSubscriptionId) {
        // 1. Find the subscription by its primary key
        UserSubscription userSubscription = userSubscriptionRepository
                .findById(userSubscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subscription not found with ID " + userSubscriptionId
                ));

        // 2. Verify it belongs to the same user
        if (!userSubscription.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You cannot unsubscribe from another user's subscription");
        }

        // 3. If it's already EXPIRED, decide how you want to handle it (maybe do nothing or throw)
        if (userSubscription.getSubscriptionStatus() == SubscriptionStatus.EXPIRED) {
            throw new IllegalStateException("Subscription is already expired");
        }

        // 4. Mark it as EXPIRED
        userSubscription.setSubscriptionStatus(SubscriptionStatus.EXPIRED);

        // 5. If it was ACTIVE, end it immediately
        if (userSubscription.getSubscriptionStatus() == SubscriptionStatus.ACTIVE) {
            userSubscription.setEndDate(LocalDateTime.now());
        }

        // 6. Save the updated subscription
        userSubscriptionRepository.save(userSubscription);
    }


    @Override
    @Transactional
    public void activateSubscriptionByTransactionReference(String transactionReferenceId) {
        // 1. Find the subscription using transactionReferenceId
        UserSubscription subscription = userSubscriptionRepository
                .findByTransactionReferenceId(transactionReferenceId)
                .orElseThrow(() ->
                        new RuntimeException("Subscription not found for transaction reference ID: " + transactionReferenceId)
                );

        // 2. Check if user already has an ACTIVE subscription
        boolean userAlreadyHasActive = userSubscriptionRepository
                .existsByUserAndSubscriptionStatus(subscription.getUser(), SubscriptionStatus.ACTIVE);

        // 3. Update status accordingly
        if (userAlreadyHasActive) {
            // If there's already an active subscription, this one is QUEUED
            subscription.setSubscriptionStatus(SubscriptionStatus.QUEUED);
        } else {
            // Otherwise, make this subscription ACTIVE
            subscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        }

        // 4. Save changes
        userSubscriptionRepository.save(subscription);

        System.out.println("Subscription "
                + subscription.getSubscriptionStatus()
                + " for Transaction Reference ID: "
                + transactionReferenceId);
    }




}
