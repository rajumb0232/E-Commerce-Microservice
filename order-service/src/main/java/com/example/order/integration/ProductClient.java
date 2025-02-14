package com.example.order.integration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class ProductClient {
    private final RestTemplate restTemplate;

    /**
     * Checks if a product with the given ID has enough quantity available.
     * @param productId The ID of the product to check.
     * @param requestedQuantity The quantity to check availability for.
     * @return true if the product exists and the product-service confirms availability; false otherwise.
     */
    public boolean isProductAvailable(Long productId, int requestedQuantity) {
        String productServiceUrl = "http://product-service/api/v1/products/"
                + productId + "/availability?quantity=" + requestedQuantity;
        try {
            ResponseEntity<ProductAvailabilityResponse> response = restTemplate
                    .getForEntity(productServiceUrl, ProductAvailabilityResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // The product-service responds with a custom availability payload
                // containing a boolean indicating availability.
                return response.getBody().isAvailable();
            }
        } catch (Exception ex) {
            // In real usage, handle exceptions properly
            // (e.g., log them, throw custom exceptions, implement retries, etc.)
        }
        return false;
    }

    /**
     * Example response model returned from product-service indicating availability.
     */
    @Setter
    @Getter
    private static class ProductAvailabilityResponse {
        private boolean available;

    }
}
