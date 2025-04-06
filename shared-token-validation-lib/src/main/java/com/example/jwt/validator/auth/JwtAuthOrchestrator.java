package com.example.jwt.validator.auth;

import com.example.jwt.validator.util.ClaimNames;
import com.example.jwt.validator.util.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public class JwtAuthOrchestrator {
    private boolean isValid;
    private String username;
    private String role;
    private TokenType type;

    private final HttpServletRequest request;
    private final TokenParser parser;

    public JwtAuthOrchestrator(TokenParser parser, HttpServletRequest request) {
        this.parser = parser;
        this.request = request;
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
     * @return true if the authentication is successful, false otherwise.
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
     * Updates the security context with the username and role. <br>
     */
    private void updateSecurityContext() {
        log.debug("Updating security context with username: {}", username);

        var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        var authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        authToken.setDetails(request);

        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.debug("Security context updated successfully.");
    }

    /**
     * Throws an exception if the authentication is not successful. <br>
     *
     * @param exceptionSupplier a Supplier that returns a RuntimeException type object. <br>
     */
    public void throwIfUnauthenticated(Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!isValid) {
            throw exceptionSupplier.get();
        }
    }

    private FailedAuthResponse failedAuthResponse;

    /**
     * Sets the failed authentication response. <br>
     *
     * @param FailedAuthResponseSupplier a Supplier that returns a FailedAuthResponse object. <br>
     * @return this instance to further configure the authentication process.
     */
    public JwtAuthOrchestrator handleUnauthenticated(Supplier<FailedAuthResponse> FailedAuthResponseSupplier) {
        if (!isValid) {
            this.failedAuthResponse = FailedAuthResponseSupplier.get();
        }
        return this;
    }

    /**
     * Sets the failed authentication response and responds. <br>
     *
     * @param response the HttpServletResponse object to respond to.
     * @throws IOException if an I/O error occurs during the response writing. <br>
     */
    public void with(HttpServletResponse response) throws IOException {
        if (failedAuthResponse != null) {
            response.setStatus(failedAuthResponse.getStatus());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getOutputStream(), this.constructResponseMap());
        }
    }

    /**
     * Constructs a Map object using the {@link FailedAuthResponse} instance. <br>
     * ensures only valid data is included in the response map.
     *
     * @return a Map object containing the failed authentication response. <br>
     */
    private Map<String, Object> constructResponseMap() {
        Map<String, Object> responseMap = new HashMap<>();

        if (failedAuthResponse.getStatus() >= 400) responseMap.put("status", failedAuthResponse.getStatus());
        if (failedAuthResponse.getMessage() != null) responseMap.put("message", failedAuthResponse.getMessage());
        if (failedAuthResponse.getError() != null) responseMap.put("error", failedAuthResponse.getError());
        if (failedAuthResponse.getAdditionalInfo() != null)
            responseMap.put("additionalInfo", failedAuthResponse.getAdditionalInfo());
        return responseMap;
    }
}
