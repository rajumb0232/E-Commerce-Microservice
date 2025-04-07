package com.rajugowda.jwt.validator.exceptions;

public class PublicKeyForTokenNotFoundException extends RuntimeException{
    public PublicKeyForTokenNotFoundException(String message) {
        super(message);
    }
}
