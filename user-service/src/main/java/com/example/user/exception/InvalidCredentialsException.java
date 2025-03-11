package com.example.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidCredentialsException extends RuntimeException {
    private final String message;
}
