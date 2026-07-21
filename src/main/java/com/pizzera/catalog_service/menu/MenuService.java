package com.pizzera.catalog_service.menu;

import com.pizzera.catalog_service.ingredient.LocationIngredientRepository;
import com.pizzera.catalog_service.location.LocationNotFoundException;
import com.pizzera.catalog_service.location.LocationRepository;
import com.pizzera.catalog_service.product.ProductRepository;
import com.pizzera.catalog_service.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final LocationIngredientRepository locationIngredientRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "menu", key = "#locationId")
    public MenuResponse getMenuForLocation(Long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new LocationNotFoundException(locationId);
        }

        Set<Long> unavailableIds = new HashSet<>(
                locationIngredientRepository.findUnavailableIngredientIds(locationId)
        );

        var allProducts = productRepository.findAllWithIngredients();

        int totalFiltered = 0;
        var availableProducts = new ArrayList<ProductResponse>();

        for (var product : allProducts) {
            boolean hasUnavailableIngredient = product.getIngredients().stream()
                    .anyMatch(pi -> unavailableIds.contains(pi.getIngredient().getId()));

            if (hasUnavailableIngredient) {
                totalFiltered++;
            } else {
                availableProducts.add(new ProductResponse(product));
            }
        }

        return new MenuResponse(availableProducts, totalFiltered);
    }
}