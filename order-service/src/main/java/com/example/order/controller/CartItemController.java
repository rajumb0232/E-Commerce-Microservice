package com.example.order.controller;

import com.example.order.model.CartItem;
import com.example.order.service.CartItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.base-url}")
@AllArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping("/cart-items/{itemId}/products/{productId}")
    public ResponseEntity<CartItem> createCartItem(@PathVariable Long itemId, @PathVariable Long productId, @RequestParam int quantity) {
        CartItem cartItem = cartItemService.createCartItem(itemId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }
}
