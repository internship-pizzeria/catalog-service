package com.pizzera.catalog_service.ingredient;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ingredient availability status at a specific location")
public record LocationIngredientResponse(
        @Schema(description = "Ingredient ID", example = "5")
        Long id,
        @Schema(description = "Ingredient name", example = "Pepperoni")
        String name,
        @Schema(description = "Whether the ingredient is available at this location", example = "true")
        boolean available
) {
    static LocationIngredientResponse from(LocationIngredient li) {
        return new LocationIngredientResponse(li.getId(), li.getIngredient().getName(), li.isAvailable());
    }
}