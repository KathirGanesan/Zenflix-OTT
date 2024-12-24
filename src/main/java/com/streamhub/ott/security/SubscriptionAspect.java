package com.streamhub.ott.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.streamhub.ott.repository.UserSubscriptionRepository;

@Aspect
@Component
public class SubscriptionAspect {

    private final UserSubscriptionRepository userSubscriptionRepository;

    public SubscriptionAspect(UserSubscriptionRepository userSubscriptionRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @Before("@annotation(com.streamhub.ott.security.RequiresSubscription) && args(userId, ..)")
    public void checkSubscription(JoinPoint joinPoint, Long userId) {
        // Check if the current user is an admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            return; // Skip subscription check for admins
        }

        boolean hasActiveSubscription = userSubscriptionRepository.existsByUserIdAndActiveTrue(userId);

        if (!hasActiveSubscription) {
            throw new AccessDeniedException("You must have an active subscription to access this resource.");
        }
    }

}

