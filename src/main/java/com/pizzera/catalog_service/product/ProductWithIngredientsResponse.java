package com.pizzera.catalog_service.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public record ProductWithIngredientsResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        List<Long> ingredientIds
) implements Serializable {
}
