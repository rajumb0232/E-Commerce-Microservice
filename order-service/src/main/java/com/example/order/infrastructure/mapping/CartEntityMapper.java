package com.example.order.infrastructure.mapping;

import com.example.order.domain.model.CartItem;
import com.example.order.infrastructure.database.CartItemEntity;
import com.example.order.infrastructure.mapping.rules.IgnoreAuditsMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @IgnoreAuditsMapping
    CartItemEntity toEntity(CartItem cartItem);

    CartItem toCartItem(CartItemEntity cartItemEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @IgnoreAuditsMapping
    void toCartItem(CartItemEntity cartItemEntity, @MappingTarget CartItem cartItem);
}
