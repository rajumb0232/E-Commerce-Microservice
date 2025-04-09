package com.example.order.api;

import com.example.order.application.dto.OrderItemResponse;
import com.example.order.application.dto.OrderResponse;
import com.example.order.application.facade.OrderFacade;
import com.example.order.domain.model.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("${app.base-url}")
public class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(String username) {
        OrderResponse response = orderFacade.createOrder(username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse response = orderFacade.findOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/orders/order-items/{itemId}")
    public ResponseEntity<OrderItemResponse> updateOrderStatus(@PathVariable Long itemId, @RequestParam OrderStatus orderStatus) {
        OrderItemResponse response = orderFacade.updateOrderStatus(itemId, orderStatus);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/orders/order-items/{itemId}")
    public ResponseEntity<OrderItemResponse> cancelOrder(@PathVariable Long itemId) {
        OrderItemResponse response = orderFacade.cancelOrder(itemId);
        return ResponseEntity.ok(response);
    }
}
