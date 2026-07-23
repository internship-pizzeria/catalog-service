package com.pizzera.catalog_service.product;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductResponse(Long id, String name, String description, BigDecimal price) implements Serializable {
}
