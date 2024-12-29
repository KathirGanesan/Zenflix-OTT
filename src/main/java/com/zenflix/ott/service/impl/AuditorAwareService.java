package com.zenflix.ott.service.impl;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class AuditorAwareService implements AuditorAware<String> {
   
    public Optional<String> getCurrentAuditor() {
        // Get the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // If no authentication is present or the user is anonymous, return "System"
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("System"); // Default value for system tasks
        }

        // Otherwise, return the username of the authenticated user
        return Optional.of(authentication.getName());
    }
}