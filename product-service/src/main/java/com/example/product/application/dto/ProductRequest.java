package com.example.product.application.dto;

public record ProductRequest(
        String title,
        String description,
        double price,
        int stock,
        String category
) {
}
