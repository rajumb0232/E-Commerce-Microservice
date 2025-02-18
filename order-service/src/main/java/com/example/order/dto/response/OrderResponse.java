package com.example.order.dto.response;

import com.example.order.model.CartItem;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private LocalDate orderDate;
    private String status;
    private List<CartItem> orderItems;
    private Double deliveryCost;
    private Double totalAmount;
    private Double totalPayableAmount;
    private String paymentMethod;
    private Long customerId;
    private String deliveryAddress;
}
