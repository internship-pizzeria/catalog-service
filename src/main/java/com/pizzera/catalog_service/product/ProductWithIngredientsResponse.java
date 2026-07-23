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
    static ProductWithIngredientsResponse from(Product product) {
        return new ProductWithIngredientsResponse(
                product.getId(), product.getName(), product.getDescription(), product.getPrice(),
                product.getIngredients().stream()
                        .map(pi -> pi.getIngredient().getId())
                        .toList()
        );
    }
}
