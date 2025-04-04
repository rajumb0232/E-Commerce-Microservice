package com.example.order.infrastructure.database.adapters;

import com.example.order.domain.model.CartItem;
import com.example.order.infrastructure.mapping.CartEntityMapper;
import com.example.order.infrastructure.database.repositories.CartItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CartItemRepositoryAdapter implements com.example.order.domain.repository.CartItemRepository {
    private final CartItemRepository cartItemRepository;
    private final CartEntityMapper cartEntityMapper;

    @Override
    public CartItem save(CartItem cartItem) {
        var entity = cartEntityMapper.toEntity(cartItem);
        entity = cartItemRepository.save(entity);

        // mapping updated data to a new CartItem object
        return cartEntityMapper.toCartItem(entity);
    }

    @Override
    public Optional<CartItem> findByProductId(Long productId) {
        return cartItemRepository.findByProductId(productId)
                .map(cartEntityMapper::toCartItem);
    }

    @Override
    public Optional<CartItem> findById(long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .map(cartEntityMapper::toCartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public boolean existById(Long cartItemId) {
        return cartItemRepository.existsById(cartItemId);
    }

    @Override
    public List<CartItem> findAllByCreatedBy(String username) {
        return cartItemRepository.findAllByCreatedBy(username)
                .stream()
                .map(cartEntityMapper::toCartItem)
                .toList();
    }
}
