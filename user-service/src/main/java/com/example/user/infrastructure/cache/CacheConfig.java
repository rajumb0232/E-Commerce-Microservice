package com.example.user.infrastructure.cache;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class CacheConfig {

    private final CacheRequirements cacheRequirements;
    private final List<ComplexCacheRequirement> complexCacheRequirements;

    private final RedisSerializationContext.SerializationPair<Object> serializer;

    public CacheConfig(CacheRequirements cacheRequirements, List<ComplexCacheRequirement> complexCacheRequirements) {
        this.cacheRequirements = cacheRequirements;
        this.complexCacheRequirements = complexCacheRequirements;
    }

    {
        serializer = RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer());
    }


    @Bean
    CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        var defaultConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(serializer)
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();

        var cacheConfigurations = generateCacheConfigurations();
        appendComplexCaches(cacheConfigurations);

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
                        requirement -> generateCacheConfiguration(
                                requirement.getCacheName(),
                                requirement.getTtlMinutes()
                        )
                ));
    }

    private RedisCacheConfiguration generateCacheConfiguration(String cacheName, Long ttlMinute) {
        log.info("Generating cache: {}", cacheName);
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(serializer)
                .entryTtl(Duration.ofMinutes(ttlMinute))
                .disableCachingNullValues();
    }

    private void appendComplexCaches(Map<String, RedisCacheConfiguration> cacheConfigurations) {
        complexCacheRequirements.forEach(complexCacheRequirement -> {
            log.info("Generating complex cache: {}", complexCacheRequirement.getCacheName());
            cacheConfigurations.put(
                    complexCacheRequirement.getCacheName(),
                    generateCacheConfiguration(
                            complexCacheRequirement.getCacheName(),
                            complexCacheRequirement.getTtlMinutes()
                    )
            );
        });
    }

}
