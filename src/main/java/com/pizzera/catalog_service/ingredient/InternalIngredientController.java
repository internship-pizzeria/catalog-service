package com.pizzera.catalog_service.ingredient;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internal/locations/{locationId}/ingredients")
@RequiredArgsConstructor
public class InternalIngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public List<LocationIngredientResponse> getAvailability(@PathVariable Long locationId) {
        return ingredientService.getAvailabilityForLocation(locationId);
    }

    @PatchMapping("/{ingredientId}")
    public LocationIngredient toggleAvailability(@PathVariable Long locationId,
                                                 @PathVariable Long ingredientId) {
        return ingredientService.toggleAvailability(locationId, ingredientId);
    }
}