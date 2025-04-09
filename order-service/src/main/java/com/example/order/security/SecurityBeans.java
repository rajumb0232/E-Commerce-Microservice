package com.example.order.security;

import com.rajugowda.jwt.validator.contracts.PublicKeysPoolService;
import com.rajugowda.jwt.validator.secret.PublicKeyMetaData;
import com.rajugowda.jwt.validator.util.CacheName;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
@AllArgsConstructor
public class SecurityBeans {

    private final CacheManager cacheManager;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    PublicKeysPoolService publicKeysPoolService() {
        var cache = cacheManager.getCache(CacheName.PUBLIC_KEY_POOL);

        if(cache != null) {
            log.debug("Cache found with name: {}", CacheName.PUBLIC_KEY_POOL);

            return keyId -> {
                var publicKeyMetaData = cache.get(keyId, PublicKeyMetaData.class);
                if (publicKeyMetaData != null) {
                    log.debug("Public key metadata found for key ID: {}", keyId);
                    return publicKeyMetaData;
                } else {
                    log.debug("Public key metadata not found for key ID: {}", keyId);
                    return null;
                }
            };
        } else log.error("Cache not found with name: {}", CacheName.PUBLIC_KEY_POOL);

        return null;
    }
}
