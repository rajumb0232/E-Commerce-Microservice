package com.example.user.security.jwt;

import com.example.user.security.jwt.secret.Vault;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class TokenGenerator {

    private final Vault vault;

    /**
     * Generates a JWT token with the specified claims and validity duration.
     *
     * @param claims   a map of claims to include in the token payload
     * @param issuedAt the token's issue time (in milliseconds)
     * @param expiration the token's validity duration (in milliseconds)
     * @return the generated JWT token as a compact string
     * @throws RuntimeException if token generation fails
     */
    public String generateToken(Map<String, Object> claims, Date issuedAt, Date expiration) {
        try {
            String token = Jwts.builder()
                    .setHeaderParam(JwtStatics.PUB_KEY_ID, vault.getCurrentPublicKeyId())
                    .setClaims(claims)
                    .setIssuedAt(issuedAt)
                    .setExpiration(expiration)
                    .signWith(vault.getPrivateKey(), SignatureAlgorithm.RS256)
                    .compact();
            log.info("Token generated successfully");
            return token;
        } catch (Exception e) {
            log.error("Error generating token: {}", e.getMessage());
            throw new RuntimeException("Token generation failed", e);
        }
    }
}
