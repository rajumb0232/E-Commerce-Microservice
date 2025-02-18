package com.example.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnorderedCartItemsNotFoundException extends RuntimeException {
    private final String message;
}
