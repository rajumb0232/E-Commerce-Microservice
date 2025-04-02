package com.example.order.infrastructure.integration;

import com.example.order.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductClientAdapter implements com.example.order.application.integration.ProductClient {

    private final ProductClient productClient;

    @Override
    public boolean checkProductAvailability(Long id, int quantity) {
        return productClient.checkProductAvailability(id, quantity);
    }

    @Override
    public Product getProductById(Long id) {
        return productClient.getProductById(id);
    }
}
