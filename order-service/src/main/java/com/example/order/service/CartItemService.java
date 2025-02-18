package com.example.order.service;

import com.example.order.exception.ProductOutOfStockException;
import com.example.order.integration.ProductClient;
import com.example.order.model.CartItem;
import com.example.order.repository.CartItemRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class CartItemService {

    private final ProductClient productClient;
    private final CartItemRepository cartItemRepository;

    @Transactional
//    @Retryable(maxAttempts = 3, value = {ProductOutOfStockException.class})
    public CartItem createCartItem(
            @NonNull Long productId,
            @Min(value = 1, message = "Minimum quantity allowed is 1")
            @Max(value = 100, message = "Maximum quantity allowed is 100")
            int quantity) {

        log.info("Attempting to create cart item for productId: {} with quantity: {}", productId, quantity);

        validateProductAvailability(productId, quantity);

        return cartItemRepository.findByProductIdAndOrdered(productId, false)
                .map(item -> updateExistingCartItem(item, quantity))
                .orElseGet(() -> createNewCartItem(productId, quantity));
    }

    private void validateProductAvailability(Long productId, int quantity) {
            boolean isAvailable = productClient.checkProductAvailability(productId, quantity);
            if (!isAvailable) {
                log.error("Product {} may not exit or not available with requested quantity {}", productId, quantity);
                throw new ProductOutOfStockException(
                        String.format("Failed to add product ID %d with quantity %d to cart - insufficient stock or product may not exist.",
                                productId, quantity));
            }
            log.info("Product availability confirmed for productId: {} with quantity: {}", productId, quantity);
    }

    private CartItem updateExistingCartItem(CartItem item, int quantity) {
        log.info("Updating existing cart item with id: {} to quantity: {}", item.getId(), quantity);
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    private CartItem createNewCartItem(Long productId, int quantity) {
        log.info("Creating new cart item for productId: {} with quantity: {}", productId, quantity);
        CartItem cartItem = CartItem.builder()
                .productId(productId)
                .quantity(quantity)
                .ordered(false)
                .build();
        return cartItemRepository.save(cartItem);
    }

    public CartItem updateCartItem(long cartItemId, int quantity) {
        log.info("Updating cart item with id: {} to quantity: {}", cartItemId, quantity);
        return cartItemRepository.findById(cartItemId)
                .map(item -> updateExistingCartItem(item, quantity))
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));
    }

    public void deleteCartItem(Long id) {
        log.info("Deleting cart item with id: {}", id);
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));

        cartItemRepository.delete(cartItem);
    }
}