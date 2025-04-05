package com.example.product.application.dto;

import java.time.LocalDate;

public record ProductResponse(
        Long id,
        String title,
        String description,
        double price,
        int stock,
        String category,
        LocalDate createdAt,
        LocalDate updatedAt
) {
}
