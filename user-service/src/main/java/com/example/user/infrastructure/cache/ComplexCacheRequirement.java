package com.example.user.infrastructure.cache;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ComplexCacheRequirement {
    private String cacheName;
    private Long ttlMinutes;
}
