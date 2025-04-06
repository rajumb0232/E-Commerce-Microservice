package com.example.validator.exceptions;

public class PublicKeyForTokenNotFoundException extends RuntimeException{
    public PublicKeyForTokenNotFoundException(String message) {
        super(message);
    }
}
