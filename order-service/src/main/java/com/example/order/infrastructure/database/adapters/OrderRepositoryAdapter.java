package com.example.order.infrastructure.database.adapters;

import com.example.order.domain.model.Order;
import com.example.order.domain.repository.OrderRepository;
import com.example.order.infrastructure.database.repositories.OrderJpaRepository;
import com.example.order.infrastructure.mapping.OrderEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;

    @Override
    public Order save(Order order) {
        var entity = orderEntityMapper.toOrderEntity(order);
        entity = orderJpaRepository.save(entity);

        // Mapping back the updated data to a new Order object
        return orderEntityMapper.toOrderDomain(entity);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId)
                .map(orderEntityMapper::toOrderDomain);
    }
}
