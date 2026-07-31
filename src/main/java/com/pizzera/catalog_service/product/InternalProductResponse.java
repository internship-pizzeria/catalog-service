package com.pizzera.catalog_service.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Product info for internal service communication")
public record InternalProductResponse(
        @Schema(description = "Product ID", example = "1")
        Long id,
        @Schema(description = "Product name", example = "Margherita")
        String name,
        @Schema(description = "Product price in PLN", example = "25.00")
        BigDecimal price,
        @Schema(description = "Whether the product is available at the given location", example = "true")
        Boolean available) {

    static InternalProductResponse from(Product product, Boolean available) {
        return new InternalProductResponse(product.getId(), product.getName(), product.getPrice(), available);
    }
}
