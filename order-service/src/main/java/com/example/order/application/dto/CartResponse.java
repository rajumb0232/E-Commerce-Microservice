package com.example.order.application.dto;

import java.util.List;

public record CartResponse(
        List<CartItemResponse> items
) {
}
