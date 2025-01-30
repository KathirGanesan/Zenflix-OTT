package com.zenflix.ott.controller;

import com.zenflix.ott.dto.SubscriptionPlanDTO;
import com.zenflix.ott.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plans")
public class SubscriptionPlanController {
    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionPlanController(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can create subscription plans
    public ResponseEntity<SubscriptionPlanDTO> createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO) {
        SubscriptionPlanDTO createdSubscription = subscriptionPlanService.createSubscriptionPlan(subscriptionPlanDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdSubscription);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // Accessible by both users and admins
    public ResponseEntity<SubscriptionPlanDTO> getSubscriptionPlanById(@PathVariable Long id) {
        SubscriptionPlanDTO subscription = subscriptionPlanService.getSubscriptionPlanById(id);
        return ResponseEntity.ok(subscription);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can view all subscription plans
    public ResponseEntity<List<SubscriptionPlanDTO>> getAllSubscriptionPlans() {
        List<SubscriptionPlanDTO> subscriptions = subscriptionPlanService.getAllSubscriptionPlans();
        return ResponseEntity.ok(subscriptions);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can update subscription plans
    public ResponseEntity<SubscriptionPlanDTO> updateSubscriptionPlan(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO
    ) {
        SubscriptionPlanDTO updatedSubscription = subscriptionPlanService.updateSubscriptionPlan(id, subscriptionPlanDTO);
        return ResponseEntity.ok(updatedSubscription);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can delete subscription plans
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionPlanService.deleteSubscriptionPlan(id);
        return ResponseEntity.noContent()
                .build();
    }

}
