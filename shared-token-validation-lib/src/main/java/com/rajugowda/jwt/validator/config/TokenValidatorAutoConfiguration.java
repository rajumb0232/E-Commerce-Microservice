package com.rajugowda.jwt.validator.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.rajugowda.jwt.validator")
@Slf4j
public class TokenValidatorAutoConfiguration {

    @PostConstruct
    public void logInit() {
        log.info("✅ Shared Token Validation Lib AutoConfigured Successfully.");
    }

}
