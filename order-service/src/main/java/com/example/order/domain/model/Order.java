package com.example.order.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
public class Order {
    private Long orderId;
    private List<OrderItem> orderItems;
    private Instant createdAt;
    private Instant updatedAt;

    public static Order createNew(List<OrderItem> orderItems) {
        return Order.builder()
                .orderItems(orderItems)
                .build();
    }
}
