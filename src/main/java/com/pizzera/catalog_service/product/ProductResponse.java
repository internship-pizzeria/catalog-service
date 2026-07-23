package com.pizzera.catalog_service.product;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String description, BigDecimal price,
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
