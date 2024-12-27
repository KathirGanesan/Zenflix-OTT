package com.streamhub.ott.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.streamhub.ott.repository.UserSubscriptionRepository;

@Component
@Aspect
public class RequiresSubscriptionAspect {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Pointcut("@annotation(com.streamhub.ott.security.RequiresSubscription)")
    public void requiresSubscription() {
    }

    @Around("requiresSubscription()")
    public Object checkSubscription(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("You must be logged in to access this resource");
        }

        String email = authentication.getName(); 
        boolean hasActiveSubscription = userSubscriptionRepository.existsByUser_EmailAndActiveTrue(email);

        if (!hasActiveSubscription) {
            throw new AccessDeniedException("Active subscription is required to access this resource");
        }

        return joinPoint.proceed();
    }
}
