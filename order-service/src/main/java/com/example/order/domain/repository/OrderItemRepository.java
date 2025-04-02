package com.example.order.domain.repository;

import com.example.order.domain.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository {

    OrderItem save(OrderItem orderItem);
    Optional<OrderItem> findById(long itemId);

    List<OrderItem> saveAll(List<OrderItem> orderItems);
}
