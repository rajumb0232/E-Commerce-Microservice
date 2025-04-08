package com.rajugowda.jwt.validator.auth.orchestrate;

import com.rajugowda.jwt.validator.auth.TokenParser;
import com.rajugowda.jwt.validator.exceptions.InvalidJwtException;
import com.rajugowda.jwt.validator.util.ClaimNames;
import com.rajugowda.jwt.validator.util.TokenType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * An orchestrator dedicated to handling JWT-based authentication flows.
 * <p>
 * {@link JwtAuthOrchestrator} extends {@link AuthOrchestrator} by leveraging a {@link TokenParser}
 * to parse JWT tokens, extract claims, and then update the security context if validation succeeds.
 * This orchestrator supports different token types (e.g., ACCESS, REFRESH) by checking appropriately
 * named cookies.
 * </p>
 *
 * <h2>Usage</h2>
 * <ul>
 *   <li>
 *     Instantiate {@link JwtAuthOrchestrator} with a {@link TokenParser} and {@link HttpServletRequest}.
 *   </li>
 *   <li>
 *     Call {@link #forTokenType(TokenType)} to specify which token (e.g., {@code ACCESS} or {@code REFRESH})
 *     should be validated.
 *   </li>
 *   <li>
 *     Invoke {@link #orchestrate()} to run the validation.
 *     Upon success, the user is authenticated within the security context.
 *     Upon failure, any subsequent steps can handle or respond to the invalid state.
 *   </li>
 * </ul>
 */
@Slf4j
public class JwtAuthOrchestrator extends AuthOrchestrator {

    private TokenType type;
    private final TokenParser parser;

    /**
     * Constructs a {@link JwtAuthOrchestrator} with the provided {@link TokenParser} and request.
     *
     * @param parser  the {@link TokenParser} responsible for parsing JWT tokens.
     * @param request the {@link HttpServletRequest} where cookies or headers may contain the token.
     */
    public JwtAuthOrchestrator(TokenParser parser, HttpServletRequest request) {
        super(request);
        this.parser = parser;
    }

    /**
     * Sets the type of token (e.g., ACCESS or REFRESH) to be validated.
     *
     * @param tokenType the {@link TokenType} this orchestrator should expect.
     * @return this {@link JwtAuthOrchestrator} instance for chaining further calls.
     */
    public JwtAuthOrchestrator forTokenType(TokenType tokenType) {
        this.type = tokenType;
        return this;
    }

    /**
     * Executes the core JWT validation and, on success, updates the security context.
     * <p>
     * This method extracts the token (if present) from the request cookies using the
     * token type, attempts validation, and sets {@link #isValid} accordingly.
     * </p>
     *
     * @return this {@link JwtAuthOrchestrator} instance for method chaining.
     */
    public JwtAuthOrchestrator orchestrate() {
        String token = extractToken(request.getCookies(), type);

        if (token != null && !token.isEmpty()) {
            log.info("Validating token for type: {}", type.getAbbreviation());
            this.validateAndInitClaims(token);
        } else {
            log.warn("Token not found in request.");
            return this;
        }

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
     * Fetches the token from the array of cookies by searching for a cookie named after the {@link TokenType}.
     *
     * @param cookies   the array of cookies from the request.
     * @param tokenType the type of token to look for (the cookie name).
     * @return the token's string value if found, or {@code null} if unavailable.
     */
    private String extractToken(Cookie[] cookies, TokenType tokenType) {
        if (cookies == null) {
            log.debug("No cookies found.");
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(tokenType.getAbbreviation())) {
                log.debug("Found token for type: {}", type.getAbbreviation());
                return cookie.getValue();
            } else log.debug("No token found for type: {}", type.getAbbreviation());
        }

        return null;
    }

    /**
     * Validates the token string using the {@link TokenParser} and populates
     * {@link AuthOrchestrator#username} and {@link AuthOrchestrator#role} from the claims.
     *
     * @param token the JWT token string to be validated.
     */
    private void validateAndInitClaims(String token) {
        if (token != null && !token.isEmpty()) {
            try {
                Claims claims = parser.parseToken(token);
                username = claims.get(ClaimNames.USERNAME, String.class);
                role = claims.get(ClaimNames.ROLE, String.class);

                isValid = (username != null && !username.isBlank()) && (role != null && !role.isBlank());
                log.info("Token validated successfully. Username: {}, Role: {}", username, role);
            } catch (Exception e) {
                log.error("Failed to validate token", e);
                isValid = false;
            }
        }
    }

    /**
     * A convenience method for orchestrating authentication against the specified token type
     * and throwing an {@link InvalidJwtException} if validation fails.
     *
     * @param type the {@link TokenType} to be validated.
     */
    public void authenticateOrThrow(TokenType type) {
        this.forTokenType(type)
                .orchestrate()
                .throwIfUnauthenticated(() -> new InvalidJwtException("Authentication Failed."));
    }
}
