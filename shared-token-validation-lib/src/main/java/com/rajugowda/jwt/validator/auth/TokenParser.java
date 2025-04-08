
package com.rajugowda.jwt.validator.auth;

import com.rajugowda.jwt.validator.exceptions.InvalidJwtException;
import com.rajugowda.jwt.validator.exceptions.InvalidPublicKeyException;
import com.rajugowda.jwt.validator.exceptions.InvalidPublicKeyIdentifierException;
import com.rajugowda.jwt.validator.exceptions.PublicKeyForTokenNotFoundException;
import com.rajugowda.jwt.validator.secret.Vault;
import com.rajugowda.jwt.validator.util.ClaimNames;
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

/**
 * Responsible for parsing JWT tokens and extracting their claims using a provided {@link Vault}.
 * This parser retrieves the corresponding public key from the Vault using the token's key ID,
 * then attempts to parse and validate the token's claims.
 */
@Component
@AllArgsConstructor
@Slf4j
public class TokenParser {

    private final Vault vault;

    /**
     * Parses the provided JWT token to extract claims using the public key retrieved from the {@link Vault}.
     *
     * @param token the JWT token string
     * @return the parsed JWT claims if the token is valid
     * @throws IllegalArgumentException             if the token is null or has invalid format
     * @throws PublicKeyForTokenNotFoundException   if a matching public key is not found
     * @throws InvalidPublicKeyIdentifierException  if the key ID in the token header is missing or invalid
     * @throws InvalidJwtException                  if parsing fails due to token expiration or corruption
     */
    public Claims parseToken(String token) throws IllegalArgumentException,
            PublicKeyForTokenNotFoundException, InvalidPublicKeyIdentifierException,
            InvalidJwtException {

        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null");
        }

        log.info("Parsing token...");

        // Extract the public key ID from the token header
        final String publicKeyId = extractPublicKeyId(token);
        // Retrieve the public key from the Vault
        final PublicKey publicKey = vault.getPublicKey(publicKeyId);

        if (publicKey == null) {
            throw new PublicKeyForTokenNotFoundException("Public key not found for token.");
        }

        // Parse token with the retrieved public key
        return parseTokenWithKey(token, publicKey);
    }

    /**
     * Parses the token using the provided public key object.
     *
     * @param token     the JWT token string
     * @param publicKey the public key to be used for verification
     * @return the valid claims if parsing was successful
     * @throws InvalidJwtException if the token is expired, malformed, or unsupported
     */
    private Claims parseTokenWithKey(String token, PublicKey publicKey) throws InvalidJwtException {
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
            log.warn("Malformed or unsupported token: {}", e.getMessage());
            throw new InvalidJwtException("Invalid token", e);
        } catch (JwtException e) {
            log.warn("General JWT error: {}", e.getMessage());
            throw new InvalidJwtException("Token parsing error", e);
        }
    }

    /**
     * Extracts the public key ID from the token's header.
     *
     * @param token the JWT token string
     * @return the public key ID found in the header
     * @throws IllegalArgumentException if the token header is not in Base64 format
     * @throws InvalidPublicKeyException if the header can't be decoded properly
     * @throws InvalidPublicKeyIdentifierException if the token header lacks a valid public key ID
     */
    private String extractPublicKeyId(String token) throws IllegalArgumentException, InvalidPublicKeyException, InvalidPublicKeyIdentifierException {
        // JWT tokens should contain header, payload, and signature
        final String[] tokenParts = token.split("\\.");
        if (tokenParts.length < 2) {
            log.error("Invalid JWT token format.");
            throw new InvalidJwtException("Invalid JWT token format");
        }

        // Decode the header (Base64 URL decoding)
        try {
            byte[] decodedHeaderBytes = Base64.getUrlDecoder().decode(tokenParts[0]);
            String headerJson = new String(decodedHeaderBytes, StandardCharsets.UTF_8);

            return retrievePublicKeyIdFromHeader(headerJson);
        } catch (IllegalArgumentException e) {
            throw new InvalidPublicKeyException("Failed to decode token header", e);
        }
    }

    /**
     * Attempts to parse the token header JSON to retrieve the public key ID using {@link ClaimNames#PUB_KEY_ID}.
     *
     * @param headerJson the token header in JSON format
     * @return the public key ID found in the header, never null or empty
     * @throws InvalidPublicKeyIdentifierException if the token header lacks a valid public key ID
     * @throws InvalidJwtException if parsing the JSON fails
     */
    private String retrievePublicKeyIdFromHeader(String headerJson)
            throws InvalidPublicKeyIdentifierException, InvalidJwtException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> headerMap = mapper.readValue(headerJson, new TypeReference<>() {});

            String publicKeyId = (String) headerMap.get(ClaimNames.PUB_KEY_ID);
            if (publicKeyId == null || publicKeyId.isEmpty()) {
                throw new InvalidPublicKeyIdentifierException("Public Key ID not found in token header");
            }

            log.info("Public Key ID extracted: {}", publicKeyId);
            return publicKeyId;
        } catch (Exception e) {
            log.error("Failed to decode token header.");
            throw new InvalidJwtException("Error parsing token header", e);
        }
    }
}
