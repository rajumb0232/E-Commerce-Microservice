package com.example.product.application.service;

import com.example.product.application.dto.ProductRequest;
import com.example.product.application.dto.ProductResponse;
import com.example.product.domain.exception.ProductNotFoundByIdException;
import com.example.product.application.mapper.ProductMapper;
import com.example.product.domain.model.Product;
import com.example.product.domain.repository.ProductRepository;
import com.example.product.shared.CustomPage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse addProduct(ProductRequest productRequest, Long sellerId) {
        Product product = productMapper.mapToProduct(productRequest);

        // Add seller ID to the product before saving
        product.setSellerId(sellerId);
        // Saving the product to the database
        productRepository.save(product);

        return productMapper.mapToProductResponse(product);
    }

    public ProductResponse getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(productMapper::mapToProductResponse)
                .orElseThrow(() -> new ProductNotFoundByIdException("Product not found with ID: " + productId));
    }

    public Page<ProductResponse> getAllProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size))
                .map(productMapper::mapToProductResponse);
    }

    public CustomPage<ProductResponse> convertToCustomPage(Page<ProductResponse> page) {
        return CustomPage.<ProductResponse>builder()
                .data(page.toList())
                .page(page.getPageable().getPageNumber())
                .size(page.getPageable().getPageSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(product -> {
                    // mapping productRequest to existing product.
                    productMapper.mapToProduct(productRequest, product);

                    productRepository.save(product);
                    return productMapper.mapToProductResponse(product);
                })
                .orElseThrow(() -> new ProductNotFoundByIdException("Product not found with ID: " + id));
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundByIdException("Product not found with ID: " + id));
        productRepository.delete(product);
    }

    public boolean checkProductAvailability(Long id, int quantity) {
        return productRepository.existsByIdAndStockGreaterThanEqual(id, quantity);
    }
}