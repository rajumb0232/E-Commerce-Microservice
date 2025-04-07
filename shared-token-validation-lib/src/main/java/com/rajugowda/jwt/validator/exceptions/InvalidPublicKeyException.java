package com.rajugowda.jwt.validator.exceptions;

public class InvalidPublicKeyException extends RuntimeException {
    public InvalidPublicKeyException(String message) {
        super(message);
    }

    public InvalidPublicKeyException(String message, Exception e) {
        super(message, e);
    }
}
