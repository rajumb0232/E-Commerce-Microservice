package com.example.order.application.facade;

import com.example.order.application.dto.OrderItemResponse;
import com.example.order.application.dto.OrderResponse;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderItem;
import com.example.order.domain.model.OrderStatus;
import com.example.order.domain.service.contracts.OrderService;
import com.example.order.application.mapping.OrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderResponse createOrder(String username) {
        Order order = orderService.createOrder(username);
        return orderMapper.mapToOrderResponse(order);
    }

    public OrderResponse findOrderById(Long orderId) {
        Order order = orderService.findOrderById(orderId);
        return orderMapper.mapToOrderResponse(order);
    }

    public OrderItemResponse updateOrderStatus(Long orderItemId, OrderStatus orderStatus) {
        OrderItem orderItem = orderService.updateOrderStatus(orderItemId, orderStatus);
        return orderMapper.mapToOrderItemResponse(orderItem);
    }

    public OrderItemResponse cancelOrder(Long orderItemId) {
        OrderItem orderItem = orderService.cancelOrder(orderItemId);
        return orderMapper.mapToOrderItemResponse(orderItem);
    }

}
