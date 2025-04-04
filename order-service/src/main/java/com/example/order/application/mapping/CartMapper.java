package com.example.order.application.mapping;

import com.example.order.application.dto.CartItemResponse;
import com.example.order.domain.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper extends InstantsMapper{

    CartItemResponse mapToCartItemResponse(CartItem cartItem);
}
