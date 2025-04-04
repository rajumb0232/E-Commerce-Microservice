package com.example.order.application.dto;


public record CartItemResponse(
        Long itemId,
        int quantity,
        Long productId,
        long createdAt,
        long updatedAt
) {
}
