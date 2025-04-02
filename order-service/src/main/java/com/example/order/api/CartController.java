package com.example.order.api;

import com.example.order.application.dto.CartItemResponse;
import com.example.order.application.facade.CartFacade;
import com.example.order.domain.service.contracts.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.base-url}")
@AllArgsConstructor
public class CartController {

    private final CartFacade cartFacade;

    @PostMapping("/cart-items/products/{productId}")
    public ResponseEntity<CartItemResponse> createCartItem(@PathVariable Long productId, @RequestParam int quantity) {
        CartItemResponse response = cartFacade.createCartItem(productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/cart-items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable long cartItemId, @RequestParam int quantity) {
        CartItemResponse updatedCartItem = cartFacade.updateCartItem(cartItemId, quantity);
        return ResponseEntity.ok(updatedCartItem);
    }

    @DeleteMapping("/cart-items/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartFacade.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }
}
