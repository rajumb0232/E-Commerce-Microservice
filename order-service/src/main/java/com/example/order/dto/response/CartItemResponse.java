package com.example.order.dto.response;

import com.example.order.integration.model.Product;
import lombok.Data;

@Data
public class CartItemResponse {
    private Long id;
    private int quantity;
    private boolean ordered;
    private Product product;
}
