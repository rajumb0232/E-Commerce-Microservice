package com.example.jwt.validator.filters;

import com.example.jwt.validator.auth.Authenticator;
import com.example.jwt.validator.util.TokenType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FilterFactory {

    private final Authenticator authenticator;

    public TokenAuthenticationFilter getAccessTokenAuthenticationFilter() {
        return TokenAuthenticationFilter.builder()
                .authenticator(authenticator)
                .authenticateTokenType(TokenType.ACCESS)
                .build();
    }

    public TokenAuthenticationFilter getRefreshTokenAuthenticationFilter() {
        return TokenAuthenticationFilter.builder()
                .authenticator(authenticator)
                .authenticateTokenType(TokenType.REFRESH)
                .build();
    }
}
