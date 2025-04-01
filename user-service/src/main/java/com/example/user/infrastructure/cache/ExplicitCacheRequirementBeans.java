package com.example.user.infrastructure.cache;

import com.example.user.shared.config.Env;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ExplicitCacheRequirementBeans {

    @Bean
    ComplexCacheRequirement publicKeyPoolRequirement(Env env) {
        Long greaterExpiration = env.getSecurity().getTokenValidity().getRefreshValidity();
        Long secretRotateInterval = env.getSecurity().getSecretRotateIntervalMillis();
        Long ttlMinutes = greaterExpiration + secretRotateInterval;
        return ComplexCacheRequirement.builder()
                .cacheName(CacheName.PUBLIC_KEYS_POOL)
                .ttlMinutes(ttlMinutes)
                .build();
    }
}
