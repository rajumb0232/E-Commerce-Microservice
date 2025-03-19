package com.example.user.application;

import com.example.user.api.dto.AuthRecord;
import com.example.user.infrastructure.config.Env;
import com.example.user.security.jwt.ClaimGen;
import com.example.user.security.jwt.TokenGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class TokenGenerationService {

    public static final String TOKEN_TYPE_ACCESS = "at";
    public static final String TOKEN_TYPE_REFRESH = "rt";

    private final TokenGenerator tokenGenerator;
    private final Env env;

    /**
     * Generates HTTP headers containing JWT tokens as cookies.
     *
     * @param authRecord the authentication authRecord containing user details
     * @return HttpHeaders with the access and refresh tokens set as cookies
     */
    public HttpHeaders getLoginCredentials(AuthRecord authRecord) {
        log.info("Granting access to user with ID: {}", authRecord.userId());
        HttpHeaders headers = new HttpHeaders();

        TokenData tokenData = getTokenData(authRecord);
        String accessToken = generateToken(tokenData, TOKEN_TYPE_ACCESS);
        String refreshToken = generateToken(tokenData, TOKEN_TYPE_REFRESH);

        // Adding cookies to the headers
        long atCookieAge = getMaxAgeSeconds(tokenData.issueAt, tokenData.accessExpiration);
        headers.add(HttpHeaders.SET_COOKIE, generateCookie(TOKEN_TYPE_ACCESS, accessToken, atCookieAge));

        long rtCookieAge = getMaxAgeSeconds(tokenData.issueAt, tokenData.refreshExpiration);
        headers.add(HttpHeaders.SET_COOKIE, generateCookie(TOKEN_TYPE_REFRESH, refreshToken, rtCookieAge));

        return headers;
    }

    /**
     * Record to store token-related data, including claims and expiration details.
     */
    public record TokenData(Map<String, Object> claims, Instant issueAt, Instant accessExpiration,
                            Instant refreshExpiration) {
    }

    /**
     * Creates token data with claims and expiration details.
     *
     * @param authRecord the authentication authRecord containing user details
     * @return TokenData object containing token claims and expiration details
     */
    private TokenData getTokenData(AuthRecord authRecord) {
        Instant issueAt = Instant.ofEpochMilli(authRecord.issueAt());
        Instant accessExpiration = Instant.ofEpochMilli(authRecord.accessExpiration());
        Instant refreshExpiration = Instant.ofEpochMilli(authRecord.refreshExpiration());

        Map<String, Object> claims = ClaimGen.builder()
                .addClaim(ClaimGen.USERNAME, authRecord.username())
                .addClaim(ClaimGen.EMAIL, authRecord.email())
                .addClaim(ClaimGen.ROLE, authRecord.role())
                .build();

        return new TokenData(claims, issueAt, accessExpiration, refreshExpiration);
    }

    /**
     * Generates a JWT token based on the provided token data and token type.
     *
     * @param tokenData the token data containing claims and expiration details
     * @param tokenType the type of token (access or refresh)
     * @return the generated JWT token as a compact string
     */
    private String generateToken(TokenData tokenData, String tokenType) {
        return tokenGenerator.generateToken(
                tokenData.claims,
                Date.from(tokenData.issueAt),
                tokenType.equals(TOKEN_TYPE_ACCESS) ? Date.from(tokenData.accessExpiration) : Date.from(tokenData.refreshExpiration));
    }

    /**
     * Calculates the maximum age for the token cookie.
     *
     * @param issueDateTime the issue date of the token
     * @param expiration    the expiration date of the token
     * @return the max age in seconds
     */
    private static long getMaxAgeSeconds(Instant issueDateTime, Instant expiration) {
        return Duration.between(issueDateTime, expiration).toSeconds();
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
        return ResponseCookie.from(name, value)
                .maxAge(maxAgeSec)
                .httpOnly(true)
                .secure(env.getSecurity().getCookie().getSecure())
                .domain(env.getSecurity().getCookie().getDomain())
                .sameSite(env.getSecurity().getCookie().getSameSite())
                .path("/")
                .toString();
    }
}
