package com.example.user.security.jwt;

import com.example.user.security.jwt.secret.SecretManager;
import com.example.user.security.jwt.secret.Vault;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class TokenParser {

    private final Vault vault;
    private final SecretManager secretManager;

    public Claims parseToken(String token) {
        log.info("Parsing token...");
        var publicKeyId = getPublicKeyId(token);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretManager.getPublicKey(publicKeyId))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error parsing token: {}", e.getMessage());
            throw new RuntimeException("Token parsing failed", e);
        }
    }

    private String getPublicKeyId(String token) {
        // Split the token into its parts: header, payload, and signature
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }

        // Decode the header (Base64 URL decoding)
        byte[] decodedBytes = Base64.getUrlDecoder().decode(tokenParts[0]);
        String headerJson = new String(decodedBytes, StandardCharsets.UTF_8);

        try {
            // Parse JSON into a Map
            ObjectMapper mapper = new ObjectMapper();
            Map headerMap = mapper.readValue(headerJson, Map.class);

            // Extract the public key id (adjust key name as needed)
            String publicKeyId = (String) headerMap.get(JwtStatics.PUB_KEY_ID);
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
