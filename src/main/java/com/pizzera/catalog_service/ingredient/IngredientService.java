package com.pizzera.catalog_service.ingredient;

import com.pizzera.catalog_service.product.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final LocationIngredientRepository locationIngredientRepository;

    @Transactional(readOnly = true)
    public List<LocationIngredient> getAvailabilityForLocation(Long locationId) {
        return locationIngredientRepository.findByLocationId(locationId);
    }

    @CacheEvict(value = "menu", key = "#locationId")
    @Transactional
    public LocationIngredient toggleAvailability(Long locationId, Long ingredientId) {
        LocationIngredient locationIngredient = locationIngredientRepository
                .findByLocationIdAndIngredientId(locationId, ingredientId)
                .orElseGet(() -> {
                    Ingredient ingredient = ingredientRepository.findById(ingredientId)
                            .orElseThrow(() -> new ProductNotFoundException(ingredientId));
                    return new LocationIngredient(locationId, ingredient, false);
                });

        locationIngredient.toggleAvailability();
        return locationIngredientRepository.save(locationIngredient);
    }
}