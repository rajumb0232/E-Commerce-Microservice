package com.example.order.application.mapping;

import com.example.order.application.dto.CartItemResponse;
import com.example.order.application.dto.ProductResponse;
import com.example.order.domain.model.CartItem;
import com.example.order.domain.model.Product;
import org.mapstruct.Mapper;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartItemResponse mapToCartItemResponse(CartItem cartItem);

    default Instant toInstant(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis);
    }

    default Long toLong(Instant instant) {
        return instant.toEpochMilli();
    }

    ProductResponse mapToProductResponse(Product product);
}
