package com.rajugowda.jwt.validator.filters;

import com.rajugowda.jwt.validator.auth.Authenticator;
import com.rajugowda.jwt.validator.util.TokenType;

public interface JwtFilter {
    void setAuthenticator(Authenticator authenticator);
    void setTokenType(TokenType tokenType);
}
