package com.example.product.exception;

public class ProductNotFoundByIdException extends RuntimeException{
    public ProductNotFoundByIdException(String message) {
        super(message);
    }
}
