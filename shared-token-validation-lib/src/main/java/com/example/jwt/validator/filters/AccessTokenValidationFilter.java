package com.example.jwt.validator.filters;

import com.example.jwt.validator.auth.Authenticator;
import com.example.jwt.validator.auth.FailedAuthResponse;
import com.example.jwt.validator.util.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Slf4j
public class AccessTokenValidationFilter extends OncePerRequestFilter {

    private final Authenticator authenticator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        authenticator.authenticateForJwt(request)
                .forTokenType(TokenType.ACCESS)
                .orchestrate()
                .handleUnauthenticated(() -> FailedAuthResponse.builder()
                        .status(HttpServletResponse.SC_UNAUTHORIZED)
                        .message("Authentication Failed.")
                        .error("Invalid access token.")
                        .additionalInfo("Please provide a valid access token.")
                        .build())
                .with(response);

        filterChain.doFilter(request, response);
    }
}
