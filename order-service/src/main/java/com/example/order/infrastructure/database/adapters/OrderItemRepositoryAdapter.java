package com.example.order.infrastructure.database.adapters;

import com.example.order.domain.model.Order;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.infrastructure.database.OrderJpaRepository;
import com.example.order.infrastructure.mapping.OrderEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrderItemRepositoryAdapter implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;

    @Override
    public Order save(Order order) {
        var entity = orderEntityMapper.toEntityOrderItem(order);
        entity = orderJpaRepository.save(entity);
        return orderEntityMapper.toDomainOrder(entity);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId)
                .map(orderEntityMapper::toDomainOrder);
    }
}
