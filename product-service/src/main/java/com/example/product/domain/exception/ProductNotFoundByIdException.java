package com.example.product.domain.exception;

public class ProductNotFoundByIdException extends RuntimeException{
    public ProductNotFoundByIdException(String message) {
        super(message);
    }
}
