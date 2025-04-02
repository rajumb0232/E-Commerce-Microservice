package com.example.order.api.exceptions;

import com.example.order.domain.exceptions.CartItemsNotFoundException;
import com.example.order.shared.ErrorStructure;
import com.example.order.shared.RestResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class CartExceptionHandler {

    private final RestResponseBuilder responseBuilder;

    @ExceptionHandler(CartItemsNotFoundException.class)
    public ResponseEntity<ErrorStructure> handleCartItemNotFound(CartItemsNotFoundException ex) {
        return responseBuilder.error(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
