package com.example.user.security.jwt;

import java.time.Instant;
import java.util.Map;

public record TokenPayload(
        TokenType tokenType,
        Map<String, Object> claims,
        Instant issueAt,
        Instant expiration
) { }
