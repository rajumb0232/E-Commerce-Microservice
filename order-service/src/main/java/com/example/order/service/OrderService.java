package com.example.order.service;

import com.example.order.exception.ProductOutOfStockException;
import com.example.order.exception.UnorderedCartItemsNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.order.dto.response.CartItemResponse;
import com.example.order.dto.response.OrderResponse;
import com.example.order.integration.ProductClient;
import com.example.order.integration.model.Product;
import com.example.order.mapper.CartItemMapper;
import com.example.order.mapper.OrderMapper;
import com.example.order.model.CartItem;
import com.example.order.model.Order;
import com.example.order.repository.CartItemRepository;
import com.example.order.repository.OrderRepository;


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final CartItemMapper cartItemMapper;
    private final ProductClient productClient;

    // TODO: The Logic should be updated to validate the user's ownership and then create the order.

    public OrderResponse createOrder() {
        log.info("Attempting to create a new order...");
        List<CartItem> cartItems = getUnOrderedCartItems();

        /* Extracting the total amount from the cart items
         * */
        BigDecimal totalAmount = ExtractTotalAmountFromItemList(cartItems);

        BigDecimal deliveryCost = new BigDecimal("10.00"); // Placeholder for actual delivery cost calculation
        Long customerId = 1L; // Placeholder for actual customer ID

        // Creating the order object and saving it to the database.
        Order order = createOrderObject(totalAmount, deliveryCost, customerId, cartItems);
        orderRepository.save(order);

        // Updating the cart items as ordered.
        UpdateCartItemAsOrdered(cartItems, order);

        // creating CartItemResponses from the cart items.
        List<CartItemResponse> cartItemResponses = mapToCartItemResponseList(cartItems);

        // returning the OrderResponse object.
        log.info("Order created successfully.");
        return getOrderResponse(order, cartItemResponses);
    }

    private List<CartItem> getUnOrderedCartItems() {
        log.info("Fetching un-ordered cart items...");
        List<CartItem> items = cartItemRepository.findAllByIsOrdered(false);
        if (items.isEmpty()) {
            log.error("No un-ordered cart items found.");
            throw new UnorderedCartItemsNotFoundException("No un-ordered cart items found.");
        }
        return items;
    }

    private OrderResponse getOrderResponse(Order order, List<CartItemResponse> cartItemResponses) {
        log.debug("Creating OrderResponse object...");
        OrderResponse orderResponse = orderMapper.mapToOrderResponse(order);
        orderResponse.setOrderItems(cartItemResponses);
        return orderResponse;
    }

    private List<CartItemResponse> mapToCartItemResponseList(List<CartItem> cartItems) {
        log.debug("Mapping cart items to CartItemResponse objects...");
        return cartItems.stream()
                .map(item -> {
                    Product product = productClient.getProductById(item.getProductId());
                    CartItemResponse response = cartItemMapper.mapToCartItemResponse(item);
                    response.setProduct(product);
                    return response;
                })
                .toList();
    }

    private void UpdateCartItemAsOrdered(List<CartItem> cartItems, Order order) {
        log.info("Updating cart items as ordered...");
        cartItems.forEach(item -> {
            item.setOrdered(true);
            item.setOrder(order);
        });
        cartItemRepository.saveAll(cartItems);
    }

    private BigDecimal ExtractTotalAmountFromItemList(List<CartItem> cartItems) {
        log.debug("Extracting total amount from cart items...");
        log.debug("Validating cart items for stock availability...");
        return cartItems.stream().map(item -> {
            Product product = productClient.getProductById(item.getProductId());

            if (product == null || product.getStock() < item.getQuantity()) {
                log.error("Failed to create order - insufficient stock");
                throw new ProductOutOfStockException("Failed to create order - insufficient stock");
            }

            return BigDecimal.valueOf(product.getPrice())
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order createOrderObject(BigDecimal totalAmount, BigDecimal deliveryCost, Long customerId, List<CartItem> cartItems) {
        log.debug("Creating order object...");
        return Order.builder()
                .status("Pending")
                .totalAmount(totalAmount)
                .paymentMethod("Credit Card")
                .deliveryAddress("123 Main St")
                .deliveryCost(deliveryCost)
                .customerId(customerId)
                .orderItems(cartItems)
                .totalPayableAmount(totalAmount.add(deliveryCost))
                .build();
    }
}
