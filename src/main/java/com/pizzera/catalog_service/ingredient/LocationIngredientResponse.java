package com.pizzera.catalog_service.ingredient;

public record LocationIngredientResponse(
        Long id,
        String name,
        boolean available
) {}