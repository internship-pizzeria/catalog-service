package com.pizzera.catalog_service.product;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String description, BigDecimal price, Long locationId) {
    public ProductResponse(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getLocation().getId()
        );
    }
}
