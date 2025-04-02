package com.example.order.domain.service.contracts;

import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderItem;
import com.example.order.domain.model.OrderStatus;

public interface OrderService {

    Order createOrder(String username);

    Order findOrderById(Long orderId);

    OrderItem updateOrderStatus(Long orderItemId, OrderStatus orderStatus);

    OrderItem cancelOrder(Long orderItemId);
}
