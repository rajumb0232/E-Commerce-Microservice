package com.example.order.application.dto;

import com.example.order.domain.model.OrderStatus;

public record OrderItemResponse(
        Long itemId,
        Long productId,
        Integer quantity,
        Long createdAt,
        Long updatedAt,
        OrderStatus orderStatus
) {
}
