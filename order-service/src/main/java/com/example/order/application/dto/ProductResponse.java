package com.example.order.application.dto;

public record ProductResponse(
        Long id,
        String title,
        String description,
        Double price,
        String category
) {
}
