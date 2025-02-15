package com.example.order.exception.handler;

import com.example.order.exception.ProductOutOfStockException;
import com.example.order.exception.util.ErrorStructure;
import com.example.order.exception.util.RestResponseBuilder;
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
