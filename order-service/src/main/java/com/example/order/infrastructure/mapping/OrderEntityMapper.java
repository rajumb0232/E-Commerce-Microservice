package com.example.order.infrastructure.mapping;

import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderItem;
import com.example.order.infrastructure.database.OrderEntity;
import com.example.order.infrastructure.database.OrderItemEntity;
import com.example.order.infrastructure.mapping.rules.IgnoreAuditsMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderEntityMapper {

    // Create a new OrderEntity from a domain Order
    @Mapping(target = "orderId", ignore = true)
    @IgnoreAuditsMapping
    @Mapping(target = "orderItems", qualifiedByName = "mapOrderItemsToEntity")
    OrderEntity toOrderEntity(Order order);

    // Update an existing OrderEntity from a domain Order
    @Mapping(target = "orderId", ignore = true)
    @IgnoreAuditsMapping
    @Mapping(target = "orderItems", qualifiedByName = "updateOrderItems")
    void updateOrderEntity(Order order, @MappingTarget OrderEntity entity);

    // Map OrderEntity back to domain Order
    Order toOrderDomain(OrderEntity entity);

    // Create a new OrderItemEntity from a domain OrderItem
    @Mapping(target = "itemId", ignore = true)
    @IgnoreAuditsMapping
    OrderItemEntity toOrderItemEntity(OrderItem orderItem);

    // Update an existing OrderItemEntity from a domain OrderItem
    @Named("updateOrderItemEntity")
    @Mapping(target = "itemId", ignore = true)
    @IgnoreAuditsMapping
    OrderItemEntity updateOrderItemEntity(OrderItem orderItem);

    // Map OrderItemEntity back to domain OrderItem
    OrderItem toOrderItemDomain(OrderItemEntity entity);

    // Use IterableMapping to convert a list of OrderItem to OrderItemEntity (for new OrderEntity)
    @Named("mapOrderItemsToEntity")
    @IterableMapping(qualifiedByName = "toOrderItemEntity")
    List<OrderItemEntity> mapOrderItemsToEntity(List<OrderItem> orderItems);

    // Use IterableMapping to convert and update a list of OrderItem (for existing OrderEntity)
    @Named("updateOrderItems")
    @IterableMapping(qualifiedByName = "updateOrderItemEntity")
    List<OrderItemEntity> updateOrderItems(List<OrderItem> orderItems);
}
