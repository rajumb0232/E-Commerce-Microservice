package com.example.product.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String title;
    private String description;
    private double price;
    private int stock;
    private String category;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
