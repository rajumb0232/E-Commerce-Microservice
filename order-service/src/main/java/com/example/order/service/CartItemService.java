package com.example.order.service;

import com.example.order.dto.response.CartItemResponse;
import com.example.order.exception.ProductOutOfStockException;
import com.example.order.integration.ProductClient;
import com.example.order.integration.model.Product;
import com.example.order.mapper.CartItemMapper;
import com.example.order.model.CartItem;
import com.example.order.repository.CartItemRepository;
import feign.FeignException;
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
    private final CartItemMapper cartItemMapper;

    @Transactional
//    @Retryable(maxAttempts = 3, value = {ProductOutOfStockException.class})
    public CartItemResponse createCartItem(
            @NonNull Long productId,
            @Min(value = 1, message = "Minimum quantity allowed is 1")
            @Max(value = 100, message = "Maximum quantity allowed is 100")
            int quantity) {

        log.info("Attempting to create cart item for productId: {} with quantity: {}", productId, quantity);

        Product product = validateProductAvailabilityAndGet(productId, quantity);

        CartItem cartItem = cartItemRepository.findByProductIdAndIsOrdered(productId, false)
                .map(item -> updateExistingCartItem(item, quantity))
                .orElseGet(() -> createNewCartItem(productId, quantity));

        CartItemResponse cartItemResponse = cartItemMapper.mapToCartItemResponse(cartItem);
        cartItemResponse.setProduct(product);
        return cartItemResponse;
    }

    private Product validateProductAvailabilityAndGet(Long productId, int quantity) {
        Product product = getProduct(productId);
        if (product == null || product.getStock() < quantity) {
            log.error("Product {} may not exit or not available with requested quantity {}", productId, quantity);
            throw new ProductOutOfStockException(
                    String.format("Failed to add product ID %d with quantity %d to cart - insufficient stock or product may not exist.",
                            productId, quantity));
        }
        log.info("Product availability confirmed for productId: {} with quantity: {}", productId, quantity);
        return product;
    }

    // TODO: needs to be updated to handle all the type of exceptions and can be globalized for better handling and reusability across multiple class.

    private Product getProduct(long productId) {
        Product product = null;
        try {
            product = productClient.getProductById(productId);
        } catch (FeignException e) {
            if(e.status() == 404) {
                log.error("Product not found with id: {}", productId);
                throw new RuntimeException("Product not found with id: " + productId);
            }
            log.error("Failed to retrieve product with id: {}", productId);
            throw new RuntimeException("Failed to retrieve product with id: " + productId);
        }
        return product;
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
                .isOrdered(false)
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