package com.example.product_service.constroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Yes I'm Up - Product Service");
    }

    // TODO: Create a new method to add a new product

    // TODO: Create a new method to find a product by ID

    // TODO: Create a new method to find all products
}
