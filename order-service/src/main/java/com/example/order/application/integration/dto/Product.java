package com.example.order.application.integration.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private long id;
    private String title;
    private String description;
    private double price;
    private int stock;
    private String category;
}
