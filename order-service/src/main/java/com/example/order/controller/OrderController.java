package com.example.order.controller;

import com.example.order.dto.response.OrderResponse;
import com.example.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${app.base-url}")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder() {
        OrderResponse response = orderService.createOrder();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
