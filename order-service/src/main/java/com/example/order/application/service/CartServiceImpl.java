package com.example.order.application.service;

import com.example.order.application.integration.ProductClient;
import com.example.order.domain.repository.CartItemRepository;
import com.example.order.domain.service.contracts.CartService;
import com.example.order.domain.exceptions.CartItemsNotFoundException;
import com.example.order.domain.model.CartItem;
import com.example.order.application.integration.dto.Product;
import com.example.order.domain.exceptions.ProductOutOfStockException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    @Override
    public CartItem createCartItem(@NonNull Long productId, int quantity) {
        log.info("Attempting to create cart item for productId: {} with quantity: {}", productId, quantity);

        Product product = this.validateProductAvailabilityAndGet(productId, quantity);
        CartItem cartItem = cartItemRepository.findByProductId(productId)
                .map(item -> {
                    item.updateQuantity(quantity);
                    return item;
                })
                .orElseGet(() -> CartItem.createNew(productId, quantity));

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(long cartItemId, int quantity) {
        return cartItemRepository.findById(cartItemId)
                .map(item -> {
                    item.updateQuantity(quantity);
                    return cartItemRepository.save(item);
                }).orElseThrow(() -> new CartItemsNotFoundException("Cart item not found with id: " + cartItemId));
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        log.info("Attempting to delete cart item with id: {}", cartItemId);
        if (cartItemRepository.existById(cartItemId)) {
            cartItemRepository.deleteById(cartItemId);
            log.info("Deleted cart item with id: {}", cartItemId);
        } else
            log.error("Failed deleting Cart Item, not found with id: {}", cartItemId);
    }

    private Product validateProductAvailabilityAndGet(Long productId, int quantity) {
        Product product = getProduct(productId);
        if (product == null || product.getStock() < quantity) {
            log.error("Product {} may not exit or not available with requested quantity {}", productId, quantity);
            throw new ProductOutOfStockException(
                    String.format("Failed to add productId ID %d with quantity %d to cart - insufficient stock or productId may not exist.",
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
            if (e.status() == 404) {
                log.error("Product not found with id: {}", productId);
                throw new RuntimeException("Product not found with id: " + productId);
            }
            log.error("Failed to retrieve productId with id: {}, status: {}, message: {}", productId, e.status(), e.getMessage());
            throw new RuntimeException("Failed to retrieve productId with id: " + productId);
        }
        return product;
    }
}
