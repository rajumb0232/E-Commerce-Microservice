package com.example.order.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    /**
     * Queries to find a cart item by its product ID and ordered status.
     *
     * @param productId The ID of the product.
     * @param status    The ordered status of the cart item.
     * @return Optional containing the cart item if found, otherwise null.
     */
    Optional<CartItemEntity> findByProductIdAndIsOrdered(Long productId, boolean status);

    /**
     * Queries to find all cart items by their ordered status.
     *
     * @param status The ordered status of the cart items.
     * @return List containing all cart items with the specified ordered status.
     */
    List<CartItemEntity> findAllByIsOrdered(boolean status);

    Optional<CartItemEntity> findByProductId(Long productId);

    Collection<CartItemEntity> findAllByCreatedBy(String username);
}
