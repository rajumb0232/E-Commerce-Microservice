package com.example.jwt.validator.filters;

import com.example.jwt.validator.auth.Authenticator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FilterFactory {

    private final Authenticator authenticator;

    public AccessTokenValidationFilter getAccessTokenValidationFilter() {
        return new AccessTokenValidationFilter(authenticator);
    }
}
