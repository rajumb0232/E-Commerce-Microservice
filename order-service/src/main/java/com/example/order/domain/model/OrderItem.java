package com.example.order.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class OrderItem {
    private Long itemId;
    private Long productId;
    private int quantity;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private OrderStatus orderStatus;

    public static OrderItem createNew(Long productId, int quantity) {
        return OrderItem.builder()
                .productId(productId)
                .quantity(quantity)
                .orderStatus(OrderStatus.PENDING)
                .build();
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

}
