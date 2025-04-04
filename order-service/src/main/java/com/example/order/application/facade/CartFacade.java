package com.example.order.application.facade;

import com.example.order.application.dto.CartItemResponse;
import com.example.order.application.mapping.CartMapper;
import com.example.order.domain.service.contracts.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartFacade {

    private final CartService cartService;
    private final CartMapper cartMapper;

    public CartItemResponse createCartItem(Long productId, int quantity) {
        var cartItem = cartService.createCartItem(productId, quantity);
        return cartMapper.mapToCartItemResponse(cartItem);
    }

    public CartItemResponse updateCartItem(long cartItemId, int quantity) {
        var cartItem = cartService.updateCartItem(cartItemId, quantity);
        return cartMapper.mapToCartItemResponse(cartItem);
    }

    public void deleteCartItem(Long id) {
        cartService.deleteCartItem(id);
    }
}
