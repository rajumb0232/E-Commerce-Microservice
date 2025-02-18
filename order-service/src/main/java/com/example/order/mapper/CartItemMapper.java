package com.example.order.mapper;

import com.example.order.dto.response.CartItemResponse;
import com.example.order.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    /**
     * Converts a CartItem object to a CartItemResponse object.
     *
     * @param cartItem The CartItem object to be converted.
     * @return The CartItemResponse object representing the CartItem.
     */
    CartItemResponse mapToCartItemResponse(CartItem cartItem);
}
