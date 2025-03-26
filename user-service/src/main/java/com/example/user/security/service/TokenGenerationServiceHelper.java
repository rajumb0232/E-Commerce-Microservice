package com.example.user.security.service;

import com.example.user.shared.config.Env;
import com.example.user.security.jwt.TokenGenerator;
import com.example.user.security.jwt.TokenType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
@AllArgsConstructor
public class TokenGenerationServiceHelper {

    private final Env env;
    private final TokenGenerator tokenGenerator;
    private final CookieGenerator cookieGenerator;

    /**
     * Creates token data with claims and expiration details.
     * <p>
     * The method automatically decides issueTime based on the token type and expiration date. <br>
     * The method also generates a cookie string with the specified name, value (which is the JWT token),
     * and along with the calculated max age.
     *
     * @param tokenType      the type of token (access or refresh)
     * @param claims         a map of claims to include in the token payload
     * @param shouldExpireAt the token's expiration date as {@link Instant}
     * @return TokenData object containing token claims and expiration details
     */
    public String issueTokenAsCookie(TokenType tokenType, Map<String, Object> claims, Instant shouldExpireAt) {
        var issueAt = calculateIssueTime(tokenType, shouldExpireAt);

        var token = tokenGenerator.generateToken(
                claims,
                Date.from(issueAt),
                Date.from(shouldExpireAt));

        return generateCookie(tokenType, shouldExpireAt, token);
    }

    /**
     * Calculates the issue date by subtracting the respective token validity from the expiration date.
     * <p>
     * Subtracting the validity of the token from the expiration ensures the right issue time.
     *
     * @param tokenType      the type of token (access or refresh)
     * @param shouldExpireAt the expiration date of the token
     * @return the issue date as {@link Instant}
     */
    private Instant calculateIssueTime(TokenType tokenType, Instant shouldExpireAt) {
        return tokenType.equals(TokenType.ACCESS)
                ? shouldExpireAt.minusSeconds(env.getSecurity().getTokenValidity().getAccessValidity())
                : shouldExpireAt.minusSeconds(env.getSecurity().getTokenValidity().getRefreshValidity());
    }

    /**
     * Generates a cookie string with the specified name, value, and max age.
     * <p>
     * The method calculates the max age in seconds based on the expiration date
     * and sets the cookie accordingly.
     *
     * @param tokenType  the type of token (access or refresh)
     * @param expiration the expiration date of the token
     * @param token      the token to be set as the cookie value
     * @return the generated cookie string suitable for the Set-Cookie header
     */
    private String generateCookie(TokenType tokenType, Instant expiration, String token) {
        var maxAge = Duration.between(Instant.now(), expiration).toSeconds();
        return cookieGenerator.generateCookie(tokenType.type(), token, maxAge);
    }
}