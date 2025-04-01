package com.example.user.infrastructure.cache;

import com.example.user.shared.config.Env;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class ExplicitRequirementBeans {

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
