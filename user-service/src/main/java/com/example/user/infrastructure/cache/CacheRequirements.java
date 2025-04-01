package com.example.user.infrastructure.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("cache")
public class CacheRequirements {
    private List<Requirement> requirements;

    @Getter
    @Setter
    public static class Requirement {
        private String cacheName;
        private Long ttlMinutes;
    }
}
