package com.pizzera.catalog_service.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

@Schema(description = "Product details")
public record ProductResponse(
        @Schema(description = "Unique product identifier", example = "1")
        Long id,
        @Schema(description = "Product name", example = "Margherita")
        String name,
        @Schema(description = "Product description", example = "Classic pizza with mozzarella and basil")
        String description,
        @Schema(description = "Product price in PLN", example = "25.00")
        BigDecimal price,
        @Schema(description = "Whether the product is available at the requested location", example = "true")
        boolean available) implements Serializable {

    static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), true);
    }

    static ProductResponse from(Product product, boolean available) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), available);
    }

    public static ProductResponse from(ProductWithIngredientsResponse product, boolean available) {
        return new ProductResponse(product.id(), product.name(), product.description(), product.price(), available);
    }
}
