package com.example.order.service;

import com.example.order.dto.response.OrderResponse;
import com.example.order.integration.ProductClient;
import com.example.order.integration.model.Product;
import com.example.order.mapper.OrderMapper;
import com.example.order.model.CartItem;
import com.example.order.model.Order;
import com.example.order.repository.CartItemRepository;
import com.example.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;

    // TODO: The Logic should be updated to validate the user's ownership and then create the order.
    
    public OrderResponse createOrder() {
        List<CartItem> cartItems = cartItemRepository.findAllByOrdered(false);

        var totalAmount = ExtractTotalAmountFromItemList(cartItems);

        double deliveryCost = 10.00; // Placeholder for actual delivery cost calculation
        Long customerId = 1L; // Placeholder for actual customer ID

        Order order = createOrderObject(totalAmount, deliveryCost, customerId, cartItems);
        orderRepository.save(order);

        return orderMapper.mapToOrderResponse(order);
    }

    private Double ExtractTotalAmountFromItemList(List<CartItem> cartItems) {
        return cartItems.stream().map(item -> {
            Product product = productClient.getProductById(item.getProductId());

            if (product == null || product.getStock() < item.getQuantity())
                throw new RuntimeException("Invalid product or insufficient stock");

            return item.getQuantity() * product.getPrice();
        }).reduce(0.0, Double::sum);
    }

    private Order createOrderObject(Double totalAmount, double deliveryCost, Long customerId, List<CartItem> cartItems) {
        return Order.builder()
                .orderDate(LocalDate.now())
                .status("Pending")
                .totalAmount(totalAmount)
                .paymentMethod("Credit Card")
                .deliveryAddress("123 Main St")
                .deliveryCost(deliveryCost)
                .customerId(customerId)
                .orderItems(cartItems)
                .totalPayableAmount(totalAmount + deliveryCost)
                .build();
    }
}
