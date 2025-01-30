package com.example.user.exception;

import com.example.user.util.ErrorStructure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    public <T> ResponseEntity<ErrorStructure<T>> errorResponse(HttpStatus status, String message, T rootCause) {
        return ResponseEntity.status(status).body(ErrorStructure.<T>builder()
                .status(status.value())
                .message(message)
                .rootCause(rootCause)
                .build());
    }

    @ExceptionHandler(UserNotFoundByIdException.class)
    public ResponseEntity<ErrorStructure<String>> handleUserNotFoundByIdException(UserNotFoundByIdException ex) {
        log.error("User not found exception: {}", ex.getMessage());
        return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "User not found by the given ID");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorStructure<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument exception: {}", ex.getMessage());
        return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid input provided");
    }
}
