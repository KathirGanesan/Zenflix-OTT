package com.zenflix.ott.controller;

import com.zenflix.ott.service.RazorpayWebhookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks")
public class RazorpayWebhookController {

    private final RazorpayWebhookService razorpayWebhookService;

    @Autowired
    public RazorpayWebhookController(RazorpayWebhookService razorpayWebhookService) {
        this.razorpayWebhookService = razorpayWebhookService;
    }

    @PostMapping("/razorpay")
    public ResponseEntity<?> handleWebhook(HttpServletRequest request) {
        try {
            // Pass raw payload and signature to the webhook service
            razorpayWebhookService.processWebhook(request);

            return ResponseEntity.ok("Webhook processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing webhook: " + e.getMessage());
        }
    }
}
