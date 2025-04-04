package com.example.order.infrastructure.database.adapters;

import com.example.order.domain.model.OrderItem;
import com.example.order.domain.repository.OrderItemRepository;
import com.example.order.infrastructure.database.repositories.OrderItemJpaRepository;
import com.example.order.infrastructure.mapping.OrderEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrderItemRepositoryAdapter implements OrderItemRepository {

    private final OrderItemJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;

    @Override
    public OrderItem save(OrderItem orderItem) {
        var entity = orderEntityMapper.toOrderItemEntity(orderItem);
        entity = orderJpaRepository.save(entity);

        // Mapping back the updated data a new OrderItem object
        return orderEntityMapper.toOrderItemDomain(entity);
    }

    @Override
    public Optional<OrderItem> findById(long itemId) {
        return orderJpaRepository.findById(itemId)
                .map(orderEntityMapper::toOrderItemDomain);
    }

    @Override
    public List<OrderItem> saveAll(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderEntityMapper::toOrderItemEntity)
                .map(orderJpaRepository::save)
                .map(orderEntityMapper::toOrderItemDomain)
                .toList();
    }
}
