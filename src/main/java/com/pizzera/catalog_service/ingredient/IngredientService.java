package com.pizzera.catalog_service.ingredient;

import com.pizzera.catalog_service.ingredient.IngredientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final LocationIngredientRepository locationIngredientRepository;

    @Transactional(readOnly = true)
    public List<LocationIngredientResponse> getAvailabilityForLocation(Long locationId) {
        List<Ingredient> allIngredients = ingredientRepository.findAll();

        Map<Long, Boolean> availabilityMap = locationIngredientRepository.findByLocationId(locationId)
                .stream()
                .collect(Collectors.toMap(
                        locationIngredient -> locationIngredient.getIngredient().getId(),
                        LocationIngredient::isAvailable
                ));

        return allIngredients.stream()
                .map(ingredient -> new LocationIngredientResponse(
                        ingredient.getId(),
                        ingredient.getName(),
                        availabilityMap.getOrDefault(ingredient.getId(), true)
                ))
                .toList();
    }

    @CacheEvict(value = "menu", key = "#locationId")
    @Transactional
    public LocationIngredient toggleAvailability(Long locationId, Long ingredientId) {
        LocationIngredient locationIngredient = locationIngredientRepository
                .findByLocationIdAndIngredientId(locationId, ingredientId)
                .orElseGet(() -> {
                    Ingredient ingredient = ingredientRepository.findById(ingredientId)
                            .orElseThrow(() -> new IngredientNotFoundException(ingredientId));
                    return new LocationIngredient(locationId, ingredient, true);
                });

        locationIngredient.toggleAvailability();
        return locationIngredientRepository.save(locationIngredient);
    }
}