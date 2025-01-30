package com.example.product_service.requestdto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String title;
    private String description;
    private double price;
    private int stock;
    private String category;
}
