package com.example.user.security.config;

import com.rajugowda.jwt.validator.contracts.PublicKeysPoolService;
import com.rajugowda.jwt.validator.secret.PublicKeyMetaData;
import com.rajugowda.jwt.validator.util.CacheName;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
@Slf4j
public class SecurityBeans {

    private final CacheManager cacheManager;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    PublicKeysPoolService publicKeysPoolService() {
        var cache = cacheManager.getCache(CacheName.PUBLIC_KEY_POOL);

        if(cache != null) {
            log.info("Cache found with name: {}", cache.getName());

            return keyId -> {
                var metaData = cache.get(keyId, PublicKeyMetaData.class);
                if(metaData != null) {
                    log.info("public key found in cache: {}, with Id: {}", CacheName.PUBLIC_KEY_POOL, keyId);
                } else log.error("public key not found in cache: {}, with Id: {}", CacheName.PUBLIC_KEY_POOL, keyId);

                return metaData;
            };
        } else log.error("Cache not found with name: {}", CacheName.PUBLIC_KEY_POOL);

        return null;
    }
}
