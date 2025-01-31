package com.zenflix.ott.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "razorpay")
@Getter
@Setter
public class RazorpayConfig {
    private String key;
    private String secret;
    private String webhookSecret;
    @PostConstruct
    public void validateConfig() {
        if (key == null || secret == null || webhookSecret == null ||
                key.isEmpty() || secret.isEmpty() || webhookSecret.isEmpty()) {
            throw new IllegalStateException("Razorpay API keys or Webhook Secret are missing. Check your configuration.");
        }
    }

}

