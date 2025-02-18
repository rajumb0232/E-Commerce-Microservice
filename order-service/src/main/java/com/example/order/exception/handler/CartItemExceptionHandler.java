package com.example.order.exception.handler;

import com.example.order.exception.ProductOutOfStockException;
import com.example.order.exception.UnorderedCartItemsNotFoundException;
import com.example.order.util.ErrorStructure;
import com.example.order.util.RestResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class CartItemExceptionHandler {

    private final RestResponseBuilder responseBuilder;

    @ExceptionHandler(UnorderedCartItemsNotFoundException.class)
    public ResponseEntity<ErrorStructure> handleUnorderedCartItemsNotFoundException(UnorderedCartItemsNotFoundException ex) {
        return responseBuilder.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
