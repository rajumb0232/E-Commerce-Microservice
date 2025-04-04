package com.example.order.application.dto;

import java.util.List;

public record OrderResponse(
        Long orderId,
        List<OrderItemResponse> orderItems,
        Long createdAt,
        Long updatedAt
) {
}
