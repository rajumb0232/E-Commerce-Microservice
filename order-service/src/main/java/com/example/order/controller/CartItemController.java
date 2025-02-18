package com.example.order.controller;

import com.example.order.dto.response.CartItemResponse;
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

    @PostMapping("/cart-items/products/{productId}")
    public ResponseEntity<CartItemResponse> createCartItem(@PathVariable Long productId, @RequestParam int quantity) {
        CartItemResponse response = cartItemService.createCartItem(productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/cart-items/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable long cartItemId, @RequestParam int quantity) {
        CartItem updatedCartItem = cartItemService.updateCartItem(cartItemId, quantity);
        return ResponseEntity.ok(updatedCartItem);
    }

    @DeleteMapping("/cart-items/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }
}
