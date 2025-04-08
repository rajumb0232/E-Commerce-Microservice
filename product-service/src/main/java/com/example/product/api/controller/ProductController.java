package com.example.product.api.controller;

import com.example.product.application.dto.ProductRequest;
import com.example.product.application.dto.ProductResponse;
import com.example.product.application.service.ProductService;
import com.example.product.shared.CustomPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.base-url}")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest, @RequestParam Long sellerId) {
        ProductResponse productResponse = productService.addProduct(productRequest, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse productResponse = productService.getProductById(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping
    public ResponseEntity<CustomPage<ProductResponse>> getAllProducts(@RequestParam int page, @RequestParam int size) {
        Page<ProductResponse> pageResponse = productService.getAllProducts(page, size);
        CustomPage<ProductResponse> responses = productService.convertToCustomPage(pageResponse);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> checkProductAvailability(@PathVariable Long id, @RequestParam int quantity) {
        boolean response = productService.checkProductAvailability(id, quantity);
        return ResponseEntity.ok(response); // Placeholder for actual implementation
    }

}
