package com.example.product.application.mapper;

import com.example.product.application.dto.ProductRequest;
import com.example.product.application.dto.ProductResponse;
import com.example.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper interface for converting Product DTOs to Entities and vice versa.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Converts a ProductRequest to a Product entity.
     *
     * @param productRequest the ProductRequest object containing product data
     * @return the corresponding Product entity
     */
    Product mapToProduct(ProductRequest productRequest);

    /**
     * Updates an existing Product entity with data from a ProductRequest.
     *
     * @param productRequest the ProductRequest containing new product data
     * @param product the Product entity to update
     */
    void mapToProduct(ProductRequest productRequest, @MappingTarget Product product);

    /**
     * Converts a Product entity to a ProductResponse DTO.
     *
     * @param product the Product entity to be converted
     * @return the corresponding ProductResponse DTO
     */
    ProductResponse mapToProductResponse(Product product);
}
