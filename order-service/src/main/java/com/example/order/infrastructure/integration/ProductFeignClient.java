package com.example.order.infrastructure.integration;

import com.example.order.application.integration.dto.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", path = "/api/v1/products")
public interface ProductFeignClient {

    @GetMapping("/{id}/availability")
    boolean checkProductAvailability(@PathVariable("id") Long id, @RequestParam("quantity") int quantity);

    @GetMapping("/{id}")
    Product getProductById(@PathVariable("id") Long id);
}
