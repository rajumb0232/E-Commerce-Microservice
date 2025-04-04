package com.example.order.infrastructure.database.repositories;

import com.example.order.infrastructure.database.entities.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemJpaRepository extends JpaRepository<OrderItemEntity, Long> {
}
