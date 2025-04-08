
package com.rajugowda.jwt.validator.filters;

import com.rajugowda.jwt.validator.auth.Authenticator;
import com.rajugowda.jwt.validator.auth.dto.FailedAuthResponse;
import com.rajugowda.jwt.validator.util.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A servlet filter that intercepts incoming requests to perform token-based authentication.
 * <p>
 * This filter runs once per request, leveraging the {@link Authenticator} to validate and
 * orchestrate authentication logic for a specific {@link TokenType} (e.g., ACCESS or REFRESH).
 * If the authentication process fails, an error response is returned to the caller with
 * relevant status and message details. <br>
 * If the authentication fails, the request doesn't proceed to the next filter. An early error is thrown to the client.
 * </p>
 *
 * <h2>Usage</h2>
 * <p>
 * Simply register this filter to the {@link org.springframework.security.web.SecurityFilterChain}
 * using the {@link FilterFactory}. <br>
 * <h2>Example</h2>
 * <pre>
 *     SecurityFilterChain filterChain = http
 *                                      .csrf(AbstractHttpConfigurer::disable)
 *                                      // Other configurations here
 *                                      .addFilterBefore(
 *                                          filterFactory.getAccessTokenAuthenticationFilter(),
 *                                          UsernamePasswordAuthenticationFilter.class
 *                                          )
 *                                      .build()
 * </pre>
 * </p>
 * <ul>
 *   <li>Extract the JWT token from the request (implementation details in {@link Authenticator}).</li>
 *   <li>Validate the token against the configured {@link TokenType}.</li>
 *   <li>Respond with a 401 (Unauthorized) and appropriate error message if the token is invalid.</li>
 *   <li>Allow the request lifecycle to proceed if token validation succeeds.</li>
 * </ul>
 *
 * @see com.rajugowda.jwt.validator.auth.Authenticator
 * @see TokenType
 */
@Slf4j
public class JwtFailFastAuthFilter extends OncePerRequestFilter implements JwtFilter{

    private Authenticator authenticator;
    private TokenType tokenType;

    @Override
    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Executes the token authentication logic once for each incoming HTTP request.
     * <p>
     * This method delegates the authentication process to the {@link Authenticator}, specifying
     * the token type to validate. If authentication fails, the filter constructs an appropriate
     * response, setting a 401 status code and an error message based on the token type. If the
     * token is valid, the filter chain continues normally.
     * </p>
     *
     * @param request     the incoming {@link HttpServletRequest}
     * @param response    the outgoing {@link HttpServletResponse}
     * @param filterChain the {@link FilterChain} for propagating the request/response
     * @throws ServletException if an error occurs while processing the request
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        boolean result = authenticator.authenticateForJwt(request)
                .forTokenType(tokenType)
                .orchestrate()
                // handling the unauthenticated case (optional in custom implementations)
                .handleUnauthenticated(() -> FailedAuthResponse.builder()
                        .status(HttpServletResponse.SC_UNAUTHORIZED)
                        .message("Authentication Failed.")
                        .error(
                                tokenType.equals(TokenType.ACCESS)
                                        ? "Invalid access token."
                                        : "Invalid refresh token."
                        )
                        .additionalInfo("Please provide a valid token.")
                        .build())
                .with(response)
                .result();

        // Only continue if authentication was successful. Otherwise, return early.
        if (result) {
            log.info("Authentication successful. Proceeding to the next filter.");
            filterChain.doFilter(request, response);
        } else
            log.error("Authentication failed. Request aborted.");
    }
}
