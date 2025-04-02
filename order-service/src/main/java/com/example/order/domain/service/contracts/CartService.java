package com.example.order.domain.service.contracts;

import com.example.order.domain.model.CartItem;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.NonNull;

public interface CartService {

    CartItem createCartItem(
            @NonNull Long productId,
                                    @Min(value = 1, message = "Minimum quantity allowed is 1")
                                    @Max(value = 100, message = "Maximum quantity allowed is 100")
                                    int quantity);

    CartItem updateCartItem(long cartItemId, int quantity);

    void deleteCartItem(Long id);
}
