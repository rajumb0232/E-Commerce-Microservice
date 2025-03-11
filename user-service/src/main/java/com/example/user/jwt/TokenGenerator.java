package com.example.user.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.Date;
import java.util.Map;

/**
 * TokenGenerator is a Spring-managed bean responsible for generating JWT access and refresh tokens.
 * <p>
 * It uses RSA (RS256) for signing tokens and leverages configuration properties for token validity.
 * </p>
 */
@Component
@Slf4j
public class TokenGenerator {


    private final PrivateKey privateKey;
    private final Long accessValidity;
    private final Long refreshValidity;

    /**
     * Constructs a TokenGenerator with the specified configuration.
     *
     * @param filePath        the file path to the RSA private key used for signing tokens
     * @param accessValidity  the validity duration (in milliseconds) for the access token
     * @param refreshValidity the validity duration (in milliseconds) for the refresh token
     * @throws RuntimeException if the private key cannot be loaded
     */
    public TokenGenerator(
            @Value("${app.security.private-key.path}") String filePath,
            @Value("${app.security.token-validity.access}") Long accessValidity,
            @Value("${app.security.token-validity.refresh}") Long refreshValidity) {
        this.accessValidity = accessValidity;
        this.refreshValidity = refreshValidity;
        privateKey = KeyLoader.loadPrivateKey(filePath);
    }

    /**
     * Generates a JWT access token with the provided claims.
     *
     * @param claims a map of claims to include in the token payload
     * @return the generated JWT access token as a compact string
     */
    public String generateAccessToken(Map<String, Object> claims) {
        log.info("Generating access token.");
        return generateToken(claims, accessValidity);
    }

    /**
     * Generates a JWT refresh token with the provided claims.
     *
     * @param claims a map of claims to include in the token payload
     * @return the generated JWT refresh token as a compact string
     */
    public String generateRefreshToken(Map<String, Object> claims) {
        log.info("Generating refresh token.");
        return generateToken(claims, refreshValidity);
    }

    /**
     * Generates a JWT token with the specified claims and validity duration.
     *
     * @param claims   a map of claims to include in the token payload
     * @param validity the token's validity duration (in milliseconds)
     * @return the generated JWT token as a compact string
     * @throws RuntimeException if token generation fails
     */
    private String generateToken(Map<String, Object> claims, Long validity) {
        try {
            long nowMillis = System.currentTimeMillis();
            long expMillis = nowMillis + validity;
            Date now = new Date(nowMillis);
            Date exp = new Date(expMillis);

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(exp)
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
            log.info("Token generated successfully");
            return token;
        } catch (Exception e) {
            log.error("Error generating token: {}", e.getMessage());
            throw new RuntimeException("Token generation failed", e);
        }
    }
}
