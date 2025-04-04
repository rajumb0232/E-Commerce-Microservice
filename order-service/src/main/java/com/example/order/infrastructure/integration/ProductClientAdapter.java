package com.example.order.infrastructure.integration;

import com.example.order.application.integration.ProductClient;
import com.example.order.application.integration.dto.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductClientAdapter implements ProductClient {

    private final ProductFeignClient productFeignClient;

    @Override
    public boolean checkProductAvailability(Long id, int quantity) {
        return productFeignClient.checkProductAvailability(id, quantity);
    }

    @Override
    public Product getProductById(Long id) {
        return productFeignClient.getProductById(id);
    }
}
