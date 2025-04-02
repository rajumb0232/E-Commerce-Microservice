package com.example.order.application.dto;


public record CartItemResponse(
        Long itemId,
        int quantity,
        ProductResponse product,
        Double totalPrice,
        long createdAt,
        long updatedAt
) {
}
