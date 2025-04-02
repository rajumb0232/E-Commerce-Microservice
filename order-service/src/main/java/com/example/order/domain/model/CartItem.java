package com.example.order.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CartItem {
    private Long itemId;
    private Product product;
    private int quantity;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Create a new CartItem instance.
     *
     * @param product the product to be added to the cart
     * @param quantity  the quantity of the product
     * @return a new CartItem instance
     */
    public static CartItem createNew(Product product, int quantity) {
        return CartItem.builder()
                .product(product)
                .quantity(quantity)
                .build();
    }

    /**
     * Updates the quantity of the CartItem.
     * @param quantity the new quantity of the product
     */
    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

}
