package com.example.order.mapper;

import com.example.order.dto.response.OrderResponse;
import com.example.order.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Maps the given Order object to an OrderResponse object.
     *
     * @param order the Order object to be mapped
     * @return orderResponse the corresponding OrderResponse object
     */
    OrderResponse mapToOrderResponse(Order order);

}
