package com.rajugowda.jwt.validator.auth;

import com.rajugowda.jwt.validator.exceptions.InvalidJwtException;
import com.rajugowda.jwt.validator.util.ClaimNames;
import com.rajugowda.jwt.validator.util.TokenType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthOrchestrator extends AuthOrchestrator {
    private TokenType type;
    private final TokenParser parser;

    public JwtAuthOrchestrator(TokenParser parser, HttpServletRequest request) {
        super(request);
        this.parser = parser;
    }

    /**
     * Sets the type of token to be validated.
     *
     * @param tokenType the type of token to be validated.
     * @return this instance to further configure the authentication process.
     */
    public JwtAuthOrchestrator forTokenType(TokenType tokenType) {
        this.type = tokenType;
        return this;
    }

    /**
     * Orchestrate the authentication process. <br>
     * The method will validate the token and update the security context if the token is valid.
     *
     */
    public JwtAuthOrchestrator orchestrate() {
        log.info("Validating token for type: {}", type.name());
        String token = extractToken(request.getCookies(), type);
        this.validateAndInitClaims(token);

        if (isValid) {
            log.debug("Token is valid. Updating security context.");
            this.updateSecurityContext();
            log.info("Authentication successful.");
        } else {
            log.error("Token is invalid. Authentication failed.");
        }
        return this;
    }

    /**
     * Extracts the token from the cookies based on the token type. <br>
     *
     * @param cookies   the cookies from the request.
     * @param tokenType the type of token to be validated.
     * @return the token if found, null otherwise.
     */
    private String extractToken(Cookie[] cookies, TokenType tokenType) {
        if (cookies == null) {
            log.debug("No cookies found.");
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(tokenType.name())) {
                log.debug("Found token for type: {}", type.name());
                return cookie.getValue();
            }
        }
        log.debug("No token found for type: {}", type.name());
        return null;
    }

    /**
     * Validates the token and initializes the claims. <br>
     *
     * @param token the token to be validated.
     */
    private void validateAndInitClaims(String token) {
        if (token == null || token.isEmpty()) {
            log.error("Invalid token: {}", token);
            isValid = false;
            return;
        }

        try {
            Claims claims = parser.parseToken(token);
            username = claims.get(ClaimNames.USERNAME, String.class);
            role = claims.get(ClaimNames.ROLE, String.class);

            isValid = (username != null && !username.isBlank())
                    && (role != null && !role.isBlank());

            log.debug("Token validated successfully. Username: {}, Role: {}", username, role);
        } catch (Exception e) {
            log.error("Failed to validate token", e);
            isValid = false;
        }
    }

    /**
     * Authenticates the token and throws an exception if the token is invalid. <br>
     *
     * @param type the type of token to be validated. <br>
     * @throws InvalidJwtException if the token is invalid. <br>
     */
    public void authenticateOrThrow(TokenType type) {
        this.forTokenType(type).orchestrate()
                .throwIfUnauthenticated(() -> new InvalidJwtException("Authentication Failed."));
    }
}
