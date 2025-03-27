package com.example.user.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
@Slf4j
public class TokenParser {

    private final PublicKey publicKey;

    public TokenParser(
            @Value("${app.security.public-key.path}") String filePath
    ) {
        log.info("Loading private key from: {}", filePath);
        this.publicKey = KeyLoader.loadPublicKey(filePath);
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error parsing token: {}", e.getMessage());
            throw new RuntimeException("Token parsing failed", e);
        }
    }
}
