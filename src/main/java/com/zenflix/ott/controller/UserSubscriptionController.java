package com.zenflix.ott.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zenflix.ott.dto.UserSubscriptionDTO;
import com.zenflix.ott.service.UserSubscriptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user-subscriptions")
public class UserSubscriptionController {

    @Autowired
    private UserSubscriptionService userSubscriptionService;

    @PostMapping("/subscribe")
    @PreAuthorize("hasRole('ADMIN') or #userSubscriptionDTO.userId == principal.id") // Only users with the USER role can subscribe
    public ResponseEntity<UserSubscriptionDTO> userSubscribe(@Valid @RequestBody UserSubscriptionDTO userSubscriptionDTO) {
        UserSubscriptionDTO createdSubscription = userSubscriptionService.userSubscribe(userSubscriptionDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdSubscription);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id") // Only admins or the user themselves
    public ResponseEntity<UserSubscriptionDTO> getUserSubscriptionById(@PathVariable Long id) {
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionService.getUserSubscriptionById(id);
        return ResponseEntity.ok(userSubscriptionDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can view all subscriptions
    public ResponseEntity<List<UserSubscriptionDTO>> getAllUserSubscriptions() {
        List<UserSubscriptionDTO> userSubscriptions = userSubscriptionService.getAllUserSubscriptions();
        return ResponseEntity.ok(userSubscriptions);
    }

    @PatchMapping("/{userId}/unsubscribe/{subscriptionId}")
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')") // Allow only the user or admin to unsubscribe
    public ResponseEntity<Void> unsubscribe(@PathVariable Long userId, @PathVariable Long subscriptionId) {
        userSubscriptionService.unsubscribe(userId, subscriptionId);
        return ResponseEntity.noContent().build(); // Respond with HTTP 204 No Content
    }


    @PatchMapping("/{id}/toggle-autorenew")
    @PreAuthorize("hasRole('ADMIN') or @userSubscriptionService.isOwner(#id, principal.id)")
    public ResponseEntity<UserSubscriptionDTO> toggleAutoRenew(@PathVariable Long id) {
        UserSubscriptionDTO updatedSubscription = userSubscriptionService.toggleAutoRenew(id);
        return ResponseEntity.ok(updatedSubscription);
    }

}

