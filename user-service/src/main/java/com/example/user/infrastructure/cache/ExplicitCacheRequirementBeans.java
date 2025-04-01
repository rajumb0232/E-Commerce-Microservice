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
        var greaterExpiration = env.getSecurity().getTokenValidity().getRefreshValidity();
        var secretRotateInterval = env.getSecurity().getSecretRotateIntervalMinutes();
        return ComplexCacheRequirement.builder()
                .cacheName(CacheName.PUBLIC_KEYS_POOL)
                .ttlMinutes(greaterExpiration + secretRotateInterval)
                .build();
    }
}
