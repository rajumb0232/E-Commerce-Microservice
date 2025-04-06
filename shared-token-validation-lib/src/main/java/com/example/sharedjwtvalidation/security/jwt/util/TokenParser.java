package com.example.sharedjwtvalidation.security.jwt.util;

import com.example.sharedjwtvalidation.security.jwt.secret.Vault;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

    public Claims parseToken(String token) {
        log.info("Parsing token...");
        var publicKeyId = extractPublicKeyId(token);
        try {
            var publicKey = vault.getPublicKey(publicKeyId);

            if (publicKey != null) {
                return parseToken(token, publicKey);
            } else {
                throw new RuntimeException("Token parsing failed");
            }
        } catch (Exception e) {
            log.error("Error parsing token: {}", e.getMessage());
            throw new RuntimeException("Token parsing failed", e);
        }
    }

    private Claims parseToken(String token, PublicKey publicKey) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String extractPublicKeyId(String token) {
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

    private String getPublicKeyId(String headerJson) {
        try {
            // Parse JSON into a Map
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> headerMap = mapper.readValue(headerJson, new TypeReference<>() {});

            // Extract the public key id (adjust key name as needed)
            String publicKeyId = (String) headerMap.get(ClaimNames.PUB_KEY_ID);
            if (publicKeyId == null || publicKeyId.isEmpty()) {
                throw new RuntimeException("Public Key ID not found in token header");
            }
            log.info("Public Key ID extracted: {}", publicKeyId);
            return publicKeyId;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing token header", e);
        }
    }
}
