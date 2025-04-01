package com.example.user.infrastructure.cache;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
@Slf4j
public class CacheConfig {

    private final CacheRequirements cacheRequirements;

    @Bean
    CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        var defaultConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();

        var cacheConfigurations = generateCacheConfigurations();

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    private Map<String, RedisCacheConfiguration> generateCacheConfigurations() {
        return cacheRequirements.getRequirements()
                .stream()
                .collect(Collectors.toMap(
                        CacheRequirements.Requirement::getCacheName,
                        requirement -> {
                            log.info("Generating cache: {}", requirement.getCacheName());
                            return RedisCacheConfiguration.defaultCacheConfig()
                                    .entryTtl(Duration.ofMinutes(requirement.getTtlMinutes()))
                                    .disableCachingNullValues();
                        }
                ));
    }

}
