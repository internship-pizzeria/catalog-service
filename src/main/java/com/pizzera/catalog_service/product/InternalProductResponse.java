package com.pizzera.catalog_service.product;

import java.math.BigDecimal;

public record InternalProductResponse(Long id, String name, BigDecimal price) {
    static InternalProductResponse from(Product product) {
        return new InternalProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
