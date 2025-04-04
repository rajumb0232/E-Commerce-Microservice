package com.example.order.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Builder
public class CartItem {
    private Long itemId;
    @Setter
    private Long productId;
    private int quantity;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Create a new CartItem instance.
     *
     * @param productId the productId that has to be added to the cart
     * @param quantity  the quantity of the productId
     * @return a new CartItem instance
     */
    public static CartItem createNew(Long productId, int quantity) {
        return CartItem.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }

    /**
     * Updates the quantity of the CartItem.
     * @param quantity the new quantity of the productId
     */
    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

}
