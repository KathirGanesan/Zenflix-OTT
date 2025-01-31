package com.zenflix.ott.security;

import com.zenflix.ott.enums.SubscriptionStatus;
import com.zenflix.ott.repository.UserSubscriptionRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RequiresSubscriptionAspect {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Pointcut("@annotation(com.zenflix.ott.security.RequiresSubscription)")
    public void requiresSubscription() {
    }

    @Around("requiresSubscription()")
    public Object checkSubscription(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("You must be logged in to access this resource");
        }

        String email = authentication.getName();
        System.out.println("Authenticated email: " + email);

        // 2. Skip subscription check for admin users
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
        System.out.println("Is user admin: " + isAdmin);

        if (isAdmin) {
            // Admin can proceed without a subscription
            return joinPoint.proceed();
        }

        // 3. Check for ACTIVE subscription
        boolean hasActiveSubscription = userSubscriptionRepository
                .existsByUser_EmailAndSubscriptionStatus(email, SubscriptionStatus.ACTIVE);
        System.out.println("Has active subscription: " + hasActiveSubscription);

        if (!hasActiveSubscription) {
            throw new AccessDeniedException("An active subscription is required to access this resource");
        }

        // 4. If the user has an active subscription, proceed with the method
        return joinPoint.proceed();
    }


}
