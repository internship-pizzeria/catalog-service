package com.pizzera.catalog_service.ingredient;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
                        LocationIngredient::isAvailable,
                        (existing, replacement) -> existing
                ));

        return allIngredients.stream()
                .map(ingredient -> new LocationIngredientResponse(
                        ingredient.getId(),
                        ingredient.getName(),
                        availabilityMap.getOrDefault(ingredient.getId(), true)
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public Set<Long> findUnavailableIngredientIds(Long locationId) {
        return new HashSet<>(locationIngredientRepository.findUnavailableIngredientIds(locationId));
    }

    @CacheEvict(value = "menu", key = "#locationId")
    @Transactional
    public LocationIngredientResponse toggleAvailability(Long locationId, Long ingredientId) {
        LocationIngredient locationIngredient = locationIngredientRepository
                .findByLocationIdAndIngredientId(locationId, ingredientId)
                .orElseGet(() -> {
                    Ingredient ingredient = ingredientRepository.findById(ingredientId)
                            .orElseThrow(() -> new IngredientNotFoundException(ingredientId));
                    return new LocationIngredient(locationId, ingredient, true);
                });

        locationIngredient.toggleAvailability();
        return LocationIngredientResponse.from(locationIngredientRepository.save(locationIngredient));
    }
}
