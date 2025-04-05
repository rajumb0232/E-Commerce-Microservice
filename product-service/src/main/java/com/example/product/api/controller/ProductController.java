package com.example.product.api.controller;

import com.example.product.requestdto.ProductRequest;
import com.example.product.responsedto.ProductResponse;
import com.example.product.application.service.ProductService;
import com.example.product.shared.CustomPage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.base-url}")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest, @RequestParam Long sellerId) {
        ProductResponse productResponse = productService.addProduct(productRequest, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productResponse);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse productResponse = productService.getProductById(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/products")
    public ResponseEntity<CustomPage<ProductResponse>> getAllProducts(@RequestParam int page, @RequestParam int size) {
        Page<ProductResponse> pageResponse = productService.getAllProducts(page, size);
        CustomPage<ProductResponse> responses = productService.convertToCustomPage(pageResponse);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productResponse);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/products/{id}/availability")
    public ResponseEntity<Boolean> checkProductAvailability(@PathVariable Long id, @RequestParam int quantity) {
        boolean response = productService.checkProductAvailability(id, quantity);
        return ResponseEntity.ok(response); // Placeholder for actual implementation
    }

}
