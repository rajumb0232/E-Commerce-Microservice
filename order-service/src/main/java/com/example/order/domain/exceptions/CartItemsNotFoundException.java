package com.example.order.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemsNotFoundException extends RuntimeException {
    private final String message;
}
