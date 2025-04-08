package com.rajugowda.jwt.validator.filters;

import com.rajugowda.jwt.validator.auth.Authenticator;
import com.rajugowda.jwt.validator.util.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * {@code JwtAuthFilter} is a non-intrusive servlet filter that attempts to authenticate requests
 * using JWT tokens (typically found in cookies). It is designed to be used in scenarios where
 * authentication is optional, and failure to authenticate should not interrupt the request processing.
 * <p>
 * This filter delegates the actual token parsing and validation to the {@link Authenticator}.
 * If a valid token is present, it sets the security context with authenticated user details.
 * If no token is present or token validation fails, the request proceeds without authentication.
 * <p>
 * This filter is typically useful for public endpoints that optionally support authenticated users.
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
 *                                          filterFactory.jwtAuthFilterForAccess(),
 *                                          UsernamePasswordAuthenticationFilter.class
 *                                          )
 *                                      .build()
 * </pre>
 * </p>
 *
 * @see com.rajugowda.jwt.validator.auth.Authenticator
 * @see TokenType
 */
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter implements JwtFilter {

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
     * Filters incoming requests to optionally authenticate based on a JWT token.
     * <p>
     * If the token of the specified type is present in the request (usually as a cookie),
     * the filter delegates validation to the {@link Authenticator}.
     * The request will continue regardless of whether authentication succeeds or fails.
     *
     * @param request     the incoming HTTP request
     * @param response    the outgoing HTTP response
     * @param filterChain the filter chain to continue request processing
     * @throws ServletException in case of servlet errors
     * @throws IOException      in case of I/O errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        /*
         * Attempt to authenticate the user based on the token type and request cookies.
         * Authentication is non-blocking: failure to authenticate will not stop the request.
         */
        authenticator.authenticateForJwt(request)
                .forTokenType(tokenType)
                .orchestrate();

        filterChain.doFilter(request, response);
    }
}
