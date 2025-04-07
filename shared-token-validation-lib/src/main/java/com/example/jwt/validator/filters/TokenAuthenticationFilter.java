package com.example.jwt.validator.filters;

import com.example.jwt.validator.auth.Authenticator;
import com.example.jwt.validator.auth.FailedAuthResponse;
import com.example.jwt.validator.util.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Builder
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final Authenticator authenticator;
    private final TokenType authenticateTokenType;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        authenticator.authenticateForJwt(request)
                .forTokenType(authenticateTokenType)
                .orchestrate()
                .handleUnauthenticated(() -> FailedAuthResponse.builder()
                        .status(HttpServletResponse.SC_UNAUTHORIZED)
                        .message("Authentication Failed.")
                        .error(
                                authenticateTokenType.equals(TokenType.ACCESS)
                                        ? "Invalid access token."
                                        : "Invalid refresh token."
                        )
                        .additionalInfo("Please provide a valid token.")
                        .build())
                .with(response);

        filterChain.doFilter(request, response);
    }
}
