package com.example.product.security;

import com.rajugowda.jwt.validator.contracts.PublicKeysPoolService;
import com.rajugowda.jwt.validator.secret.PublicKeyMetaData;
import com.rajugowda.jwt.validator.util.CacheName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ValidationLibConfig {

    @Bean
    PublicKeysPoolService tokenLocatorService(CacheManager cacheManager) {
        var cache = cacheManager.getCache("public-keys-pool");

        return keyId -> {
            if (cache == null) {
                log.warn("Cache not found for name: {}", CacheName.PUBLIC_KEYS_POOL);
                return null;
            }

            log.info("Cache found for name: {}", CacheName.PUBLIC_KEYS_POOL);

            // Let Spring handle key prefixing — don't add "public-keys-pool::" manually
            var publicKeyMetaData = cache.get(keyId, PublicKeyMetaData.class);

            if (publicKeyMetaData != null) {
                log.info("Found public key in cache for Identifier: {}", keyId);
            } else {
                log.warn("Public Key not found for Identifier: {}", keyId);
            }

            return publicKeyMetaData;
        };
    }
}
