package com.pizzera.catalog_service.product;

import java.math.BigDecimal;

public record InternalProductResponse(Long id, String name, BigDecimal price) {
    public InternalProductResponse(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}
