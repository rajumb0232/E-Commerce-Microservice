package com.example.order.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class OrderItem {
    private Long itemId;
    private Product product;
    private int quantity;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Double totalPrice;
    private OrderStatus orderStatus;

    public static OrderItem createNew(Product product, int quantity) {
        return OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .totalPrice(product.getPrice() * quantity)
                .orderStatus(OrderStatus.PENDING)
                .build();
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

}
