package com.example.order.infrastructure.database.adapters;

import com.example.order.domain.model.CartItem;
import com.example.order.infrastructure.mapping.CartEntityMapper;
import com.example.order.infrastructure.database.CartItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CartItemRepositoryAdapter implements com.example.order.domain.repository.CartItemRepository {
    private final CartItemRepository cartItemRepository;
    private final CartEntityMapper cartEntityMapper;

    @Override
    public CartItem save(CartItem cartItem) {
        var entity = cartEntityMapper.toEntity(cartItem);
        cartItemRepository.save(entity);
        // ensuring the associated Product doesn't get updated
        cartEntityMapper.toCartItem(entity, cartItem);
        return cartItem;
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
