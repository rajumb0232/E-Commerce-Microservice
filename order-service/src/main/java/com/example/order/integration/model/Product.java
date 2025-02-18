package com.example.order.integration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Product {
    private long id;
    private String title;
    private String description;
    private double price;
    private int stock;
    private String category;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
