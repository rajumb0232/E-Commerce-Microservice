package com.example.user.security.filters;

import com.example.user.security.jwt.TokenType;
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
public class AuthFilter extends OncePerRequestFilter {

    private final FilterHelper filterHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("AuthFilter is processing request: {}", request.getRequestURI());
        String token = filterHelper.getTokenFromRequestCookies(TokenType.ACCESS, request.getCookies());

        if (token != null) {
            log.info("Access token found");
            ExtractedTokenClaims claims = filterHelper.extractTokenClaims(token);

            log.info("Token type {} is valid", TokenType.ACCESS);
            filterHelper.updateSecurityContext(request, claims);

            log.info("User authenticated successfully");
        }
        filterChain.doFilter(request, response);
    }
}
