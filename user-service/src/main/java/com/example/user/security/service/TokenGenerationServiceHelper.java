package com.example.user.security.service;

import com.example.user.infrastructure.config.Env;
import com.example.user.security.jwt.TokenGenerator;
import com.example.user.security.jwt.TokenType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class TokenGenerationServiceHelper {

    private final Env env;
    private final TokenGenerator tokenGenerator;

    /**
     * Creates token data with claims and expiration details.
     *
     * @param tokenType      the type of token (access or refresh)
     * @param claims         a map of claims to include in the token payload
     * @param shouldExpireAt the token's expiration date as {@link Instant}
     * @return TokenData object containing token claims and expiration details
     */
    public String generateToken(TokenType tokenType, Map<String, Object> claims, Instant shouldExpireAt) {
        var issueAt = calculateIssueAt(tokenType, shouldExpireAt);

        var token = tokenGenerator.generateToken(
                claims,
                Date.from(issueAt),
                Date.from(shouldExpireAt));

        return generateCookie(tokenType, shouldExpireAt, token);
    }

    /**
     * Calculates the issue date for the token based on the token type and expiration date.
     *
     * @param tokenType      the type of token (access or refresh)
     * @param shouldExpireAt the expiration date of the token
     * @return the issue date as {@link Instant}
     */
    private Instant calculateIssueAt(TokenType tokenType, Instant shouldExpireAt) {
        Instant issueAt;

        switch (tokenType) {
            case ACCESS ->
                    issueAt = shouldExpireAt.minusSeconds(env.getSecurity().getTokenValidity().getAccessValidity());
            case REFRESH ->
                    issueAt = shouldExpireAt.minusSeconds(env.getSecurity().getTokenValidity().getRefreshValidity());
            default -> throw new IllegalArgumentException("Invalid token type: " + tokenType);
        }
        return issueAt;
    }

    /**
     * Calculates the maximum age for the token cookie.
     *
     * @param tokenType  the type of token (access or refresh)
     * @param expiration the expiration date of the token
     * @param token      the token to be set as the cookie value
     * @return the max age in seconds
     */
    private String generateCookie(TokenType tokenType, Instant expiration, String token) {
        var maxAge = Duration.between(Instant.now(), expiration).toSeconds();
        return this.generateCookie(tokenType.type(), token, maxAge);
    }

    /**
     * Generates a cookie string with the specified name, value, and max age.
     *
     * @param name      the name of the cookie
     * @param value     the value of the cookie (typically a JWT token)
     * @param maxAgeSec the maximum age of the cookie in seconds
     * @return the cookie string suitable for the Set-Cookie header
     */
    private String generateCookie(String name, String value, long maxAgeSec) {
        log.info("Generating cookie: {}", name);
        return ResponseCookie.from(name, value)
                .maxAge(maxAgeSec)
                .httpOnly(true)
                .secure(env.getSecurity().getCookie().getSecure())
                .domain(env.getSecurity().getCookie().getDomain())
                .sameSite(env.getSecurity().getCookie().getSameSite())
                .path("/")
                .build()
                .toString();
    }
}
