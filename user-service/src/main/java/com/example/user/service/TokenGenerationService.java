package com.example.user.service;

import com.example.user.config.Env;
import com.example.user.dto.response.AuthResponse;
import com.example.user.jwt.ClaimGen;
import com.example.user.jwt.TokenGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

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
     * @param response the authentication response containing user details
     * @return HttpHeaders with the access and refresh tokens set as cookies
     */
    public HttpHeaders getLoginCredentials(AuthResponse response) {
        log.info("Granting access to user with ID: {}", response.getUserId());
        HttpHeaders headers = new HttpHeaders();

        TokenData tokenData = getTokenData(response);
        generateAndAddAccessTokenAsCookie(tokenData, headers);
        generateAndAddRefreshTokenAsCookie(tokenData, headers);

        response.setAccessExpiration(tokenData.accessExpiration.getTime());
        response.setRefreshExpiration(tokenData.refreshExpiration.getTime());

        return headers;
    }

    /**
     * Record to store token-related data, including claims and expiration details.
     */
    public record TokenData(Map<String, Object> claims, Date issueDateTime, Date accessExpiration, Date refreshExpiration) { }

    /**
     * Creates token data with claims and expiration details.
     *
     * @param response the authentication response containing user details
     * @return TokenData object containing token claims and expiration details
     */
    private TokenData getTokenData(AuthResponse response) {
        long nowMillis = System.currentTimeMillis();
        Date issueDateTime = new Date(nowMillis);
        Date accessExpiration = new Date(nowMillis + env.getSecurity().getTokenValidity().getAccessValidity());
        Date refreshExpiration = new Date(nowMillis + env.getSecurity().getTokenValidity().getRefreshValidity());

        Map<String, Object> claims = ClaimGen.builder()
                .addClaim(ClaimGen.USERNAME, response.getUsername())
                .addClaim(ClaimGen.EMAIL, response.getEmail())
                .addClaim(ClaimGen.ROLE, response.getRole())
                .build();

        return new TokenData(claims, issueDateTime, accessExpiration, refreshExpiration);
    }

    /**
     * Generates and adds the refresh token as a cookie to the response headers.
     *
     * @param tokenData the {@link TokenData} record containing token claims and expiration details
     * @param headers the {@link HttpHeaders} to which the cookies have to be added
     */
    private void generateAndAddRefreshTokenAsCookie(TokenData tokenData, HttpHeaders headers) {
        String refreshToken = tokenGenerator.generateToken(tokenData.claims, tokenData.issueDateTime, tokenData.refreshExpiration);
        var maxAgeSeconds = getMaxAgeSeconds(tokenData.issueDateTime(), tokenData.refreshExpiration);
        headers.add(HttpHeaders.SET_COOKIE, generateCookie(TOKEN_TYPE_REFRESH, refreshToken, maxAgeSeconds));
    }

    /**
     * Generates and adds the access token as a cookie to the response headers.
     *
     * @param tokenData the {@link TokenData} record containing token claims and expiration details
     * @param headers the {@link HttpHeaders} to which the cookies have to be added
     */
    private void generateAndAddAccessTokenAsCookie(TokenData tokenData, HttpHeaders headers) {
        String accessToken = tokenGenerator.generateToken(tokenData.claims, tokenData.issueDateTime, tokenData.accessExpiration);
        var maxAgeSeconds = getMaxAgeSeconds(tokenData.issueDateTime(), tokenData.accessExpiration);
        headers.add(HttpHeaders.SET_COOKIE, generateCookie(TOKEN_TYPE_ACCESS, accessToken, maxAgeSeconds));
    }

    /**
     * Calculates the maximum age for the token cookie.
     *
     * @param issueDateTime the issue date of the token
     * @param expiration    the expiration date of the token
     * @return the max age in seconds
     */
    private static long getMaxAgeSeconds(Date issueDateTime, Date expiration) {
        long currentTimeMillis = System.currentTimeMillis();
        long bufferTime = currentTimeMillis - issueDateTime.getTime();
        return (expiration.getTime() - issueDateTime.getTime() - bufferTime) / 1000;
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
