package com.example.product.security;

import com.rajugowda.jwt.validator.contracts.TokenLocatorService;
import com.rajugowda.jwt.validator.secret.PublicKeyMetaData;
import com.rajugowda.jwt.validator.util.CacheName;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationLibConfig {

    @Bean
    TokenLocatorService tokenLocatorService(CacheManager cacheManager) {
        var cache = cacheManager.getCache((String) CacheName.PUBLIC_KEYS_POOL);

        return keyId -> {
            if (cache != null) {
                return cache.get(keyId, PublicKeyMetaData.class);
            }
            return null;
        };
    }

}
