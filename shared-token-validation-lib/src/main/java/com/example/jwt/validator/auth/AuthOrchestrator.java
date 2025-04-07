package com.example.jwt.validator.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class AuthOrchestrator {
    protected String username;
    protected String role;
    protected boolean isValid;

    protected final HttpServletRequest request;

    public AuthOrchestrator(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Updates the security context with the username and role. <br>
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
    public AuthOrchestrator handleUnauthenticated(Supplier<FailedAuthResponse> FailedAuthResponseSupplier) {
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
