package com.example.validator.jwt.util;

import com.example.validator.exceptions.InvalidJwtException;
import com.example.validator.exceptions.InvalidPublicKeyException;
import com.example.validator.exceptions.InvalidPublicKeyIdentifierException;
import com.example.validator.exceptions.PublicKeyForTokenNotFoundException;
import com.example.validator.jwt.secret.Vault;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class TokenParser {

    private final Vault vault;

    public Claims parseToken(String token) throws IllegalArgumentException,
            PublicKeyForTokenNotFoundException, InvalidPublicKeyIdentifierException,
            InvalidJwtException {

        if(token == null)
            throw new IllegalArgumentException("Token cannot be null");

        log.info("Parsing token...");
        var publicKeyId = extractPublicKeyId(token);

        var publicKey = vault.getPublicKey(publicKeyId);

        if (publicKey != null) {
            return parseToken(token, publicKey);
        } else {
            throw new PublicKeyForTokenNotFoundException("Public key not found for token.");
        }
    }

    private Claims parseToken(String token, PublicKey publicKey) throws InvalidJwtException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
            throw new InvalidJwtException("Token expired", e);
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.warn("Malformed/Unsupported token: {}", e.getMessage());
            throw new InvalidJwtException("Invalid token", e);
        } catch (JwtException e) {
            log.warn("General JWT error: {}", e.getMessage());
            throw new InvalidJwtException("Token parsing error", e);
        }
    }

    private String extractPublicKeyId(String token) throws IllegalArgumentException, InvalidPublicKeyException {
        // Split the token into its parts: header, payload, and signature
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }

        // Decode the header (Base64 URL decoding)
        byte[] decodedBytes = Base64.getUrlDecoder().decode(tokenParts[0]);
        String headerJson = new String(decodedBytes, StandardCharsets.UTF_8);

        return getPublicKeyId(headerJson);
    }

    private String getPublicKeyId(String headerJson) throws InvalidPublicKeyIdentifierException {
        try {
            // Parse JSON into a Map
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> headerMap = mapper.readValue(headerJson, new TypeReference<>() {
            });

            // Extract the public key id (adjust key name as needed)
            String publicKeyId = (String) headerMap.get(ClaimNames.PUB_KEY_ID);
            if (publicKeyId == null || publicKeyId.isEmpty()) {
                throw new InvalidPublicKeyIdentifierException("Public Key ID not found in token header");
            }

            log.info("Public Key ID extracted: {}", publicKeyId);
            return publicKeyId;
        } catch (Exception e) {
            throw new InvalidJwtException("Error parsing token header", e);
        }
    }
}
