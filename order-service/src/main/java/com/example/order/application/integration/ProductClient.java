package com.example.order.application.integration;

import com.example.order.application.integration.dto.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface ProductClient {

    boolean checkProductAvailability(@PathVariable("id") Long id, @RequestParam("quantity") int quantity);
    Product getProductById(@PathVariable("id") Long id);

}
