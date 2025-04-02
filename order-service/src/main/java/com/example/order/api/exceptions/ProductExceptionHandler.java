package com.example.order.api.exceptions;

import com.example.order.domain.exceptions.ProductOutOfStockException;
import com.example.order.shared.ErrorStructure;
import com.example.order.shared.RestResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class ProductExceptionHandler {

    private final RestResponseBuilder responseBuilder;

    @ExceptionHandler(ProductOutOfStockException.class)
    public ResponseEntity<ErrorStructure> handleProductOutOfStockException(ProductOutOfStockException ex) {
        return responseBuilder.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
