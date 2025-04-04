package com.example.order.infrastructure.mapping;

import com.example.order.application.mapping.InstantsMapper;
import com.example.order.domain.model.CartItem;
import com.example.order.infrastructure.database.entities.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartEntityMapper extends InstantsMapper {

    CartItemEntity toEntity(CartItem cartItem);

    CartItem toCartItem(CartItemEntity cartItemEntity);

}
