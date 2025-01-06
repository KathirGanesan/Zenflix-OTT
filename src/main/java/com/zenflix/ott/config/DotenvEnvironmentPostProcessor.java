package com.zenflix.ott.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> {
            // Let Spring see them as system properties
            System.setProperty(entry.getKey(), entry.getValue());
        });
        System.out.println("DotenvEnvironmentPostProcessor: .env loaded.");
    }
}