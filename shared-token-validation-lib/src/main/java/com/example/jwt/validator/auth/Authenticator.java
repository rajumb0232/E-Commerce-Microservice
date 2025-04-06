package com.example.jwt.validator.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * This class handles the authentication of JWT tokens.
 * Example:
 * <pre>
 *     boolean result = jwtAuthenticator.authenticateJwt(request)
 *                                      .forTokenType(TokenType.ACCESS)
 *                                      .orchestrate();
 * </pre>
 */
@Component
@AllArgsConstructor
@Slf4j
public class Authenticator {

    private final TokenParser tokenParser;

    /**
     * The method is used to authenticate a JWT token from the request.
     * @param request the HTTP request that has to be authenticated.
     * @return a {@link JwtAuthOrchestrator} instance to further configure the authentication process.
     */
    public JwtAuthOrchestrator authenticateJwt(HttpServletRequest request) {
        return new JwtAuthOrchestrator(tokenParser, request);
    }
}
