package com.zenflix.ott.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;

    @PostConstruct
    public void validateConfig() {
        if (secret == null || secret.isEmpty() ) {
            throw new IllegalStateException("JWT Secret key is missing. Check your configuration.");
        }
    }
}
