package com.example.order.controller;

import com.example.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${app.base-url}")
public class OrderController {
    private OrderService orderService;

    // TODO: Implement the logic to handle the order creation
    /*
     * In order to create order, the endpoint should receive the CartItem ID, The CartItem should
     * be found based on the ID and the order should be created with the cart items. */
}
