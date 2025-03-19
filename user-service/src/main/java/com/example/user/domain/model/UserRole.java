package com.example.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum UserRole {
    SELLER(List.of(Privilege.SELLER_READ,
            Privilege.SELLER_WRITE,
            Privilege.CUSTOMER_WRITE,
            Privilege.CUSTOMER_READ)),
    CUSTOMER(List.of(Privilege.CUSTOMER_READ, Privilege.CUSTOMER_WRITE));

    private final List<Privilege> privileges;
}
