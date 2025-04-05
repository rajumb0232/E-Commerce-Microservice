package com.example.product.domain.repository;

import com.example.product.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Check if a product exists with the given ID and has more stock than the specified quantity.
     * @param id The ID of the product to check.
     * @param quantity The minimum stock quantity required.
     * @return true if the product exists and has more stock than the specified quantity, false otherwise.*/
    boolean existsByIdAndStockGreaterThanEqual(Long id, int quantity);
}
