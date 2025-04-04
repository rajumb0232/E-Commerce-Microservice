package com.example.order.infrastructure.database.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private int quantity;

    @CreatedDate
    @Column(name = "created_at")
    private Long createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "created_by")
    private String createdBy;
}
