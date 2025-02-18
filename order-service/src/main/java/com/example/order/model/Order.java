package com.example.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private LocalDate orderDate;
    private String status;

    @OneToMany(mappedBy = "order")
    private List<CartItem> orderItems;

    private String paymentMethod;
    private BigDecimal totalAmount;
    private BigDecimal deliveryCost;
    private BigDecimal totalPayableAmount;

    // Reference to the customer record
    private Long customerId;
    private String deliveryAddress;

}
