package com.example.order.integration;

import com.example.order.integration.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${app.client.product.name}", url = "${app.client.product.url}")
public interface ProductClient {

    @GetMapping("/products/{id}/availability")
    boolean checkProductAvailability(@PathVariable("id") Long id, @RequestParam("quantity") int quantity);

    @GetMapping("/products/{id}")
    Product getProductById(@PathVariable("id") Long id);
}
