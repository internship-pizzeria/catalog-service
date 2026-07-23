package com.pizzera.catalog_service.ingredient;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ingredient food category")
public enum IngredientCategory {
    @Schema(description = "Meat products (e.g. pepperoni, ham, chicken)")
    MEAT,
    @Schema(description = "Vegetables (e.g. mushrooms, peppers, olives)")
    VEGETABLE,
    @Schema(description = "Cheese varieties (e.g. mozzarella, parmesan)")
    CHEESE,
    @Schema(description = "Sauces (e.g. tomato sauce, BBQ sauce)")
    SAUCE,
    @Schema(description = "Other ingredients (e.g. basil, oregano)")
    OTHER
}
