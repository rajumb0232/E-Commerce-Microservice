package com.example.product.api.exception;

import com.example.product.domain.exception.ProductNotFoundByIdException;
import com.example.product.shared.ErrorStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    private <T> ResponseEntity<ErrorStructure<T>> handleException(HttpStatus status, String message, T rootCause) {
        return ResponseEntity
                .status(status)
                .body(ErrorStructure.<T>builder()
                        .status(status.value())
                        .message(message)
                        .rootCause(rootCause)
                        .build());
    }

    @ExceptionHandler(ProductNotFoundByIdException.class)
    private ResponseEntity<ErrorStructure<String>> handleProductNotFoundByIdException(ProductNotFoundByIdException ex) {
        return handleException(HttpStatus.NOT_FOUND, ex.getMessage(), "Product not found by the given Id");
    }
}
