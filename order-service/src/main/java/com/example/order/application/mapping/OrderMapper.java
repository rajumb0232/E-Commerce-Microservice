package com.example.order.application.mapping;

import com.example.order.application.dto.OrderItemResponse;
import com.example.order.application.dto.OrderResponse;
import com.example.order.domain.model.Order;
import com.example.order.domain.model.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper extends InstantsMapper {

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

}
