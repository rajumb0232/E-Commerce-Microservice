package com.example.order.application.dto;

import com.example.order.domain.model.OrderStatus;

import java.time.Instant;

public record OrderItemResponse(
        Long itemId,
        ProductResponse product,
        Integer quantity,
        Long createdAt,
        Long updatedAt,
        Double totalPrice,
        OrderStatus orderStatus
) {
}
