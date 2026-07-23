package com.pizzera.catalog_service.ingredient;

public record LocationIngredientResponse(
        Long id,
        String name,
        boolean available
) {
    static LocationIngredientResponse from(LocationIngredient li) {
        return new LocationIngredientResponse(li.getId(), li.getIngredient().getName(), li.isAvailable());
    }
}