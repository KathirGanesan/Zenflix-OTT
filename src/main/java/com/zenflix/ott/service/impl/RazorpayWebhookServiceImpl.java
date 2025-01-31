package com.zenflix.ott.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenflix.ott.config.RazorpayConfig;
import com.zenflix.ott.config.Utils;
import com.zenflix.ott.entity.Transaction;
import com.zenflix.ott.enums.PaymentStatus;
import com.zenflix.ott.repository.TransactionRepository;
import com.zenflix.ott.repository.UserSubscriptionRepository;
import com.zenflix.ott.service.RazorpayWebhookService;
import com.zenflix.ott.service.TransactionService;
import com.zenflix.ott.service.UserSubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RazorpayWebhookServiceImpl implements RazorpayWebhookService {

    private final TransactionService transactionService;
    private final UserSubscriptionService userSubscriptionService;
    private final RazorpayConfig razorpayConfig;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public RazorpayWebhookServiceImpl(TransactionService transactionService,
                                      UserSubscriptionService userSubscriptionService,
                                      RazorpayConfig razorpayConfig, UserSubscriptionRepository userSubscriptionRepository, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.userSubscriptionService = userSubscriptionService;
        this.razorpayConfig = razorpayConfig;
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void processWebhook(HttpServletRequest request) {
        try {
            // Step 1: Read the raw body from the request
            String payloadBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            // Extract the signature from headers
            String signature = request.getHeader("X-Razorpay-Signature");

            System.out.println("🔹 Verifying Webhook Signature...");
            System.out.println("🔹 Received Signature: " + signature);

            // Step 2: Verify Signature
            String webhookSecret = razorpayConfig.getWebhookSecret();
            boolean isValid = Utils.verifyWebhookSignature(payloadBody, signature, webhookSecret);

            if (!isValid) {
                throw new SecurityException("Invalid Razorpay webhook signature");
            }

            // Step 3: Convert payloadBody into a JSON object
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payload = objectMapper.readValue(payloadBody, Map.class);

            // Step 4: Identify Event Type
            String event = (String) payload.get("event");

            switch (event) {
                case "payment_link.paid":
                    handlePaymentCaptured(payload);
                    break;
                case "payment.failed":
                case "payment_link.expired":
                    handlePaymentFailed(payload);
                    break;
                default:
                    System.out.println("🔹 Ignoring event: " + event);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing Razorpay webhook: " + e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    private void handlePaymentCaptured(Map<String, Object> payload) {
        if (!"payment_link.paid".equals(payload.get("event"))) {
            System.out.println("Ignoring event: " + payload.get("event"));
            return;
        }

        Map<String, Object> payloadData = (Map<String, Object>) payload.get("payload");
        Map<String, Object> paymentLinkWrapper = (Map<String, Object>) payloadData.get("payment_link");
        Map<String, Object> paymentWrapper = (Map<String, Object>) payloadData.get("payment");

        Map<String, Object> paymentLinkData = (Map<String, Object>) paymentLinkWrapper.get("entity");
        Map<String, Object> paymentData = (Map<String, Object>) paymentWrapper.get("entity");

        String transactionReferenceId = (String) paymentLinkData.get("reference_id");
        String razorpayPaymentId = (String) paymentData.get("id"); // Razorpay Payment ID

        // 🔹 Check if this transaction was already processed
        Optional<Transaction> existingTransaction = transactionRepository.findByTransactionReferenceId(transactionReferenceId);
        if (existingTransaction.isPresent() && existingTransaction.get().getPaymentStatus() == PaymentStatus.SUCCESS) {
            System.out.println("🔹 Payment already processed. Skipping duplicate webhook.");
            return; // Exit early to prevent duplicate processing
        }

        System.out.println("Processing Payment Captured (Payment Link Paid): Transaction Reference ID = "
                + transactionReferenceId + ", Razorpay Payment ID = " + razorpayPaymentId);

        // 🔹 Step 1: Mark the transaction as successful
        transactionService.markTransactionSuccess(transactionReferenceId, razorpayPaymentId);

        // 🔹 Step 2: Activate the associated subscription
        userSubscriptionService.activateSubscriptionByTransactionReference(transactionReferenceId);
    }


    @SuppressWarnings("unchecked")
    private void handlePaymentFailed(Map<String, Object> payload) {
        Map<String, Object> payloadData = (Map<String, Object>) payload.get("payload"); // Explicit casting

        String transactionReferenceId;

        // Extract `reference_id` from the payload for all cases
        if (payloadData.containsKey("payment_link")) {
            // If the event is related to Payment Link Expiry
            Map<String, Object> paymentLinkWrapper = (Map<String, Object>) payloadData.get("payment_link");
            Map<String, Object> paymentLinkData = (Map<String, Object>) paymentLinkWrapper.get("entity");
            transactionReferenceId = (String) paymentLinkData.get("reference_id");
        } else if (payloadData.containsKey("payment")) {
            // If the event is related to Payment Failure
            Map<String, Object> paymentWrapper = (Map<String, Object>) payloadData.get("payment");
            Map<String, Object> paymentData = (Map<String, Object>) paymentWrapper.get("entity");
            transactionReferenceId = (String) paymentData.get("reference_id");
        } else {
            System.err.println("Unknown Webhook Event: Missing payment or payment_link entity.");
            return;
        }

        System.err.println("Payment Failed or Expired: Transaction Reference ID = " + transactionReferenceId);

        // Step 1: Mark the transaction as failed using `transaction_reference_id`
        transactionService.markTransactionFailed(transactionReferenceId);
    }





}
