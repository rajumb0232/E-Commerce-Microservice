package com.example.order.infrastructure.database.repositories;

import com.example.order.infrastructure.database.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
