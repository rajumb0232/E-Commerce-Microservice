package com.example.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Long id;
    // Reference to the product record
    private Long productId;
    private Long quantity;
    private boolean ordered;
}
