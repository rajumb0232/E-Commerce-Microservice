package com.example.order.infrastructure.mapping;

import com.example.order.application.mapping.InstantsMapper;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderItem;
import com.example.order.infrastructure.database.entities.OrderEntity;
import com.example.order.infrastructure.database.entities.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderEntityMapper  extends InstantsMapper {

    OrderEntity toOrderEntity(Order order);

    Order toOrderDomain(OrderEntity entity);

    OrderItemEntity toOrderItemEntity(OrderItem orderItem);

    OrderItem toOrderItemDomain(OrderItemEntity entity);

    default List<OrderItemEntity> mapOrderItemsDomainToEntity(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toOrderItemEntity)
                .toList();
    }

    default List<OrderItem> mapOrderItemsEntityToDomain(List<OrderItemEntity> orderItems) {
        return orderItems.stream()
                .map(this::toOrderItemDomain)
                .toList();
    }

}
