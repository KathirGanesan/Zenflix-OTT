package com.zenflix.ott.service.impl;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.zenflix.ott.dto.UserSubscriptionDTO;
import com.zenflix.ott.entity.SubscriptionPlan;
import com.zenflix.ott.entity.Transaction;
import com.zenflix.ott.entity.User;
import com.zenflix.ott.entity.UserSubscription;
import com.zenflix.ott.enums.PaymentStatus;
import com.zenflix.ott.exception.ResourceNotFoundException;
import com.zenflix.ott.exception.SubscriptionConflictException;
import com.zenflix.ott.mapper.UserSubscriptionMapper;
import com.zenflix.ott.repository.SubscriptionPlanRepository;
import com.zenflix.ott.repository.TransactionRepository;
import com.zenflix.ott.repository.UserRepository;
import com.zenflix.ott.repository.UserSubscriptionRepository;
import com.zenflix.ott.service.RazorpayService;
import com.zenflix.ott.service.UserSubscriptionService;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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

        // Check for active subscriptionPlan
        UserSubscription activeSubscription = userSubscriptionRepository.findByUserAndActiveTrue(user);

        if (activeSubscription != null) {
            if (activeSubscription.getSubscriptionPlan().equals(subscriptionPlan) && activeSubscription.getIsAutoRenew()) {
                // If auto-renew is true and the same plan is being subscribed
                throw new SubscriptionConflictException("You are already subscribed to this plan.");
            } else {
                // Auto-renew is not enabled, queue the new entry
                return queueNewSubscription(user, subscriptionPlan, activeSubscription.getEndDate().plusDays(1), userSubscriptionDTO.getIsAutoRenew());
            }
        }

        // No active subscriptionPlan, create a new subscriptionPlan
//        return createNewSubscription(user, subscriptionPlan, LocalDateTime.now(), userSubscriptionDTO.getIsAutoRenew());
        return createNewSubscriptionWithPaymentLink(user, subscriptionPlan, LocalDateTime.now(), userSubscriptionDTO.getIsAutoRenew());

    }

    private UserSubscriptionDTO createNewSubscriptionWithPaymentLink(User user, SubscriptionPlan subscriptionPlan, LocalDateTime startDate, Boolean isAutoRenew) {
        LocalDateTime endDate = startDate.plusMonths(subscriptionPlan.getDurationMonths());

        // Generate `transaction_reference_id` once and use it across all entities
        String transactionReferenceId = "txn_" + System.currentTimeMillis();

        // Step 1: Create Payment Link
        String paymentUrl;
        try {
            RazorpayClient razorpayClient = razorpayService.getClient();

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", subscriptionPlan.getPrice().multiply(BigDecimal.valueOf(100)).intValue());
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("description", "Subscription payment for plan: " + subscriptionPlan.getPlanName());
            paymentLinkRequest.put("reference_id", transactionReferenceId);

            // Customer details
            JSONObject customer = new JSONObject();
            customer.put("name", user.getFirstName() + " " + user.getLastName());
            customer.put("contact", user.getPhoneNumber());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            // Notification options
            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("reminder_enable", true);
            // Create the payment link
            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
            paymentUrl = paymentLink.get("short_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Error creating Razorpay payment link: " + e.getMessage());
        }

        // Step 2: Create Transaction Entry (Pending Payment)
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setSubscriptionPlan(subscriptionPlan);
        transaction.setAmount(subscriptionPlan.getPrice());
        transaction.setPaymentStatus(PaymentStatus.PENDING);
        transaction.setTransactionDate(startDate);
        transaction.setTransactionReferenceId(transactionReferenceId);
        transactionRepository.save(transaction);

        // Step 3: Create User Subscription Entry
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setSubscriptionPlan(subscriptionPlan);
        userSubscription.setStartDate(startDate);
        userSubscription.setEndDate(endDate);
        userSubscription.setNextRenewalDate(endDate);
        userSubscription.setIsAutoRenew(isAutoRenew);
        userSubscription.setActive(false);
        userSubscription.setTransactionReferenceId(transactionReferenceId);
        userSubscriptionRepository.save(userSubscription);

        // Step 4: Map to DTO and Return
        UserSubscriptionDTO responseDTO = userSubscriptionMapper.toDTO(userSubscription);
        responseDTO.setPaymentAmount(subscriptionPlan.getPrice());
        responseDTO.setTransactionReferenceId(transaction.getTransactionReferenceId());
        responseDTO.setPaymentStatusMessage("Pending payment. Complete payment using the provided link.");
        responseDTO.setPaymentLink(paymentUrl);
        return responseDTO;
    }




    private UserSubscriptionDTO createNewSubscription(User user, SubscriptionPlan subscriptionPlan, LocalDateTime startDate, Boolean isAutoRenew) {
        LocalDateTime endDate = startDate.plusMonths(subscriptionPlan.getDurationMonths());

        // Create transaction entry
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setSubscriptionPlan(subscriptionPlan);
        transaction.setAmount(subscriptionPlan.getPrice());
        transaction.setPaymentStatus(PaymentStatus.SUCCESS);
        transaction.setTransactionDate(startDate);
        transaction.setTransactionReferenceId(UUID.randomUUID().toString());
        transactionRepository.save(transaction);

        // Create user subscriptionPlan entry
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setSubscriptionPlan(subscriptionPlan);
        userSubscription.setStartDate(startDate);
        userSubscription.setEndDate(endDate);
        userSubscription.setNextRenewalDate(endDate);
        userSubscription.setIsAutoRenew(isAutoRenew); // Set from user input
        userSubscription.setActive(true);

        UserSubscription savedUserSubscription = userSubscriptionRepository.save(userSubscription);

        // Return DTO
        UserSubscriptionDTO responseDTO = userSubscriptionMapper.toDTO(savedUserSubscription);
        responseDTO.setPaymentAmount(subscriptionPlan.getPrice());
        responseDTO.setTransactionReferenceId(transaction.getTransactionReferenceId());
        responseDTO.setPaymentStatusMessage("Transaction successful with transaction ID: " + transaction.getTransactionReferenceId());
        return responseDTO;
    }


    
    private UserSubscriptionDTO queueNewSubscription(User user, SubscriptionPlan subscriptionPlan, LocalDateTime startDate, Boolean isAutoRenew) {
        LocalDateTime endDate = startDate.plusMonths(subscriptionPlan.getDurationMonths());

        // Create user subscriptionPlan entry
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setSubscriptionPlan(subscriptionPlan);
        userSubscription.setStartDate(startDate);
        userSubscription.setEndDate(endDate);
        userSubscription.setNextRenewalDate(endDate);
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
    public List<UserSubscriptionDTO> getAllUserSubscriptions() {
        return userSubscriptionRepository.findAllByActiveTrue().stream()
                .map(userSubscription -> {
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
                })
                .collect(Collectors.toList());
    }


    @Override
    public void unsubscribe(Long userId, Long subscriptionPlanId) {
        UserSubscription userSubscription = userSubscriptionRepository.findByUserIdAndSubscriptionPlanIdAndActiveTrue(userId, subscriptionPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Active subscription not found for the given user and subscription"));

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

    @Override
    public void activateSubscriptionByTransactionReference(String transactionReferenceId) {
        // Find the subscription using transactionReferenceId
        UserSubscription subscription = userSubscriptionRepository.findByTransactionReferenceId(transactionReferenceId)
                .orElseThrow(() -> new RuntimeException("Subscription not found for transaction reference ID: " + transactionReferenceId));

        // Activate the subscription
        subscription.setActive(true);
        userSubscriptionRepository.save(subscription);

        System.out.println("Subscription activated for Transaction Reference ID: " + transactionReferenceId);
    }



}
