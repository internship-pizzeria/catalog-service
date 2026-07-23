package com.pizzera.catalog_service.product;

import java.math.BigDecimal;

public record InternalProductResponse(Long id, String name, BigDecimal price) {
}
