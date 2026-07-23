package com.pizzera.catalog_service.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Minimal product info for internal service communication")
public record InternalProductResponse(
        @Schema(description = "Product ID", example = "1")
        Long id,
        @Schema(description = "Product name", example = "Margherita")
        String name,
        @Schema(description = "Product price in PLN", example = "25.00")
        BigDecimal price) {

    static InternalProductResponse from(Product product) {
        return new InternalProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
