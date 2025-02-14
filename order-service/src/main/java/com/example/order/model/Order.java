package com.example.order.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderDate;
    private String status;
    private Double totalAmount;
    private String productDescription;
    private String productImage;
    private String shippingAddress;
    private String paymentMethod;
    private String deliveryAddress;
    private String deliveryCost;
    private String paymentStatus;

    // Reference to the product record
    private Long productId;

    // Reference to the customer record
    private Long customerId;


}
