package com.rajugowda.jwt.validator.auth.orchestrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajugowda.jwt.validator.auth.dto.FailedAuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * A base class responsible for orchestrating authentication logic and managing
 * high-level flow for handling successful and failed authentication scenarios.
 * <p>
 * This class stores relevant authentication data, updates the Spring Security
 * context upon successful validation, and returns response data or throws exceptions
 * when validation fails.
 * </p>
 *
 * <h2>Usage</h2>
 * <ul>
 *   <li>
 *     Derived classes should perform the actual token or credential validation and
 *     set {@code username}, {@code role}, and {@code isValid}.
 *   </li>
 *   <li>
 *     If {@code isValid} is set to {@code true}, {@link #updateSecurityContext()} can be
 *     used to authenticate the user within Spring Security’s context.
 *   </li>
 *   <li>
 *     If authentication fails, callers can supply a response or an exception
 *     using {@link #handleUnauthenticated(Supplier)} or {@link #throwIfUnauthenticated(Supplier)}.
 *   </li>
 * </ul>
 */
@Slf4j
public class AuthOrchestrator {

    protected String username;
    protected String role;
    protected boolean isValid;

    /**
     * The incoming HTTP request, used for gleaning additional context or details needed
     * during authentication (e.g., cookies, headers).
     */
    protected final HttpServletRequest request;

    /**
     * Constructs an {@link AuthOrchestrator} instance with the specified request.
     * 
     * @param request the {@link HttpServletRequest} to reference for authentication data.
     */
    public AuthOrchestrator(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Updates the Spring Security context with the current {@link #username} and {@link #role}.
     * <p>
     * This method should only be called after a successful validation, as indicated by {@link #isValid}.
     * </p>
     */
    protected void updateSecurityContext() {
        log.debug("Updating security context with username: {}", username);
        var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        var authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        authToken.setDetails(request);

        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.debug("Security context updated successfully.");
    }

    /**
     * Throws an exception if authentication did not succeed.
     * <p>
     * This can be used by higher-level methods to quickly short-circuit the workflow if
     * {@link #isValid} is false.
     * </p>
     *
     * @param exceptionSupplier a {@link Supplier} that returns a {@link RuntimeException} subtype.
     *                          This exception is thrown if authentication fails.
     */
    public void throwIfUnauthenticated(Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!isValid) {
            throw exceptionSupplier.get();
        }
    }

    private FailedAuthResponse failedAuthResponse;

    /**
     * Specifies the action to take if authentication has failed. Callers supply a
     * {@link FailedAuthResponse} to describe the failure state (e.g., HTTP status, error message).
     *
     * @param failedAuthResponseSupplier a {@link Supplier} producing a {@link FailedAuthResponse} instance.
     * @return this instance, to allow method chaining for further orchestration.
     */
    public AuthOrchestrator handleUnauthenticated(Supplier<FailedAuthResponse> failedAuthResponseSupplier) {
        if (!isValid) {
            this.failedAuthResponse = failedAuthResponseSupplier.get();
        }
        return this;
    }

    /**
     * If the authentication has failed, writes a failure response to the {@link HttpServletResponse}.
     * <p>
     * If {@link #failedAuthResponse} is not null, it will be used to generate a JSON output
     * with relevant error details.
     * </p>
     *
     * @param response the {@link HttpServletResponse} used to send the failure details.
     * @return this instance, to allow method chaining.
     * @throws IOException if an I/O error occurs while writing response data.
     */
    public AuthOrchestrator with(HttpServletResponse response) throws IOException {
        if (failedAuthResponse != null) {
            response.setStatus(failedAuthResponse.getStatus());
            response.setContentType(MediaType.APPLICATION_JSON.getType());
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getOutputStream(), failedAuthResponse);
        }
        return this;
    }

    /**
     * Provides the overall result of authentication.
     *
     * @return {@code true} if the authentication was successful; otherwise {@code false}.
     */
    public boolean result() {
        return isValid;
    }
}
