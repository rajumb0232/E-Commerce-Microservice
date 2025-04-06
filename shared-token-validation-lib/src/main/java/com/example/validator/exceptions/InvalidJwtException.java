package com.example.validator.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException(String message, JwtException e) {
        super(message, e);
    }

    public InvalidJwtException(String message, Exception e) {
        super(message, e);
    }
}
