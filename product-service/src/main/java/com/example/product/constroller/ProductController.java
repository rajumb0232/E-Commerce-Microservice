package com.example.product.constroller;

import com.example.product.requestdto.ProductRequest;
import com.example.product.responsedto.ProductResponse;
import com.example.product.service.ProductService;
import com.example.product.util.CustomPage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
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
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(productResponse);
    }

    @GetMapping
    public ResponseEntity<CustomPage<ProductResponse>> getAllProducts(@RequestParam int page, @RequestParam int size) {
        Page<ProductResponse> pageResponse = productService.getAllProducts(page, size);
        CustomPage<ProductResponse> responses = productService.convertToCustomPage(pageResponse);
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(responses);
    }
}
