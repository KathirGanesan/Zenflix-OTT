package com.zenflix.ott.controller;

import com.zenflix.ott.dto.UserSubscriptionDTO;
import com.zenflix.ott.service.UserSubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-subscriptions")
public class UserSubscriptionController {


    private final UserSubscriptionService userSubscriptionService;

    public UserSubscriptionController(UserSubscriptionService userSubscriptionService) {
        this.userSubscriptionService = userSubscriptionService;
    }

    @PostMapping("/subscribe")
    @PreAuthorize("hasRole('ADMIN') or #userSubscriptionDTO.userId == principal.id") // Only users with the USER role can subscribe
    public ResponseEntity<UserSubscriptionDTO> userSubscribe(@Valid @RequestBody UserSubscriptionDTO userSubscriptionDTO) {
        UserSubscriptionDTO createdSubscription = userSubscriptionService.userSubscribe(userSubscriptionDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdSubscription);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins
    public ResponseEntity<UserSubscriptionDTO> getUserSubscriptionById(@PathVariable Long id) {
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionService.getUserSubscriptionById(id);
        return ResponseEntity.ok(userSubscriptionDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can view all user subscriptions
    public ResponseEntity<List<UserSubscriptionDTO>> getAllUserSubscriptions() {
        List<UserSubscriptionDTO> userSubscriptions = userSubscriptionService.getActiveAndQueuedUserSubscriptions();
        return ResponseEntity.ok(userSubscriptions);
    }

    @PatchMapping("/{userId}/unsubscribe/{userSubscriptionId}")
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public ResponseEntity<Void> unsubscribe(
            @PathVariable Long userId,
            @PathVariable Long userSubscriptionId) {

        userSubscriptionService.unsubscribe(userId, userSubscriptionId);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }


}

