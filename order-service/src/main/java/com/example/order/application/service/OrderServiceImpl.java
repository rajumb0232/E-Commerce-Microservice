package com.example.order.application.service;

import com.example.order.application.mapping.OrderMapper;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderItem;
import com.example.order.domain.model.OrderStatus;
import com.example.order.domain.repository.CartItemRepository;
import com.example.order.domain.repository.OrderItemRepository;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.domain.service.contracts.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order createOrder(String username) {
        var orderItems = cartItemRepository.findAllByCreatedBy(username)
                .stream()
                .map(item -> OrderItem.createNew(item.getProductId(), item.getQuantity()))
                .toList();

//        orderItems = orderItemRepository.saveAll(orderItems);
        var order = Order.createNew(orderItems);
        return orderRepository.save(order);
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow();
    }

    @Override
    public OrderItem updateOrderStatus(Long orderItemId, OrderStatus orderStatus) {
        return orderItemRepository.findById(orderItemId)
                .map(item -> {
                    item.updateStatus(orderStatus);
                    return orderItemRepository.save(item);
                })
                .orElseThrow();
    }

    @Override
    public OrderItem cancelOrder(Long orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .map(item -> {
                    item.updateStatus(OrderStatus.CANCELLED);
                    return orderItemRepository.save(item);
                })
                .orElseThrow();
    }
}
