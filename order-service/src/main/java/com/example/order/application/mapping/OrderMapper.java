package com.example.order.application.mapping;

import com.example.order.application.dto.OrderItemResponse;
import com.example.order.application.dto.OrderResponse;
import com.example.order.application.dto.ProductResponse;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderItem;
import com.example.order.domain.model.Product;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Maps the given Order object to an OrderResponse object.
     *
     * @param order the Order object to be mapped
     * @return orderResponse the corresponding OrderResponse object
     */
    OrderResponse mapToOrderResponse(Order order);

    default List<OrderItemResponse> toOrderItemResponses(List<OrderItem> orderItems){
        return orderItems.stream().map(this::mapToOrderItemResponse).toList();
    }

    OrderItemResponse mapToOrderItemResponse(OrderItem orderItem);

    default Long toEpochMillis(Instant instant){
        return instant.toEpochMilli();
    }

    default Instant toInstant(Long epochMillis){
        return Instant.ofEpochMilli(epochMillis);
    }

    ProductResponse toProductResponse(Product product);
}
