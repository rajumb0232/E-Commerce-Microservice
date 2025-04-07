package com.rajugowda.jwt.validator.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.rajugowda.jwt.validator")
public class TokenValidatorAutoConfiguration {

    @PostConstruct
    public void logInit() {
        System.out.println("✅ TokenValidatorAutoConfiguration loaded");
    }

}
