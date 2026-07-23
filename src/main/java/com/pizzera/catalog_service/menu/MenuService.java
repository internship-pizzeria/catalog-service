package com.pizzera.catalog_service.menu;

import com.pizzera.catalog_service.ingredient.IngredientService;
import com.pizzera.catalog_service.location.LocationService;
import com.pizzera.catalog_service.product.ProductResponse;
import com.pizzera.catalog_service.product.ProductService;
import com.pizzera.catalog_service.product.ProductWithIngredientsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final ProductService productService;
    private final LocationService locationService;
    private final IngredientService ingredientService;

    @Transactional(readOnly = true)
    @Cacheable(value = "menu", key = "#locationId")
    public MenuResponse getMenuForLocation(Long locationId) {
        locationService.ensureExists(locationId);

        Set<Long> unavailableIds = ingredientService.findUnavailableIngredientIds(locationId);

        var allProducts = productService.findAllWithIngredients();

        int totalFiltered = 0;
        var availableProducts = new ArrayList<ProductResponse>();

        for (ProductWithIngredientsResponse product : allProducts) {
            boolean hasUnavailableIngredient = product.ingredientIds().stream()
                    .anyMatch(unavailableIds::contains);

            if (hasUnavailableIngredient) {
                totalFiltered++;
            } else {
                availableProducts.add(new ProductResponse(
                        product.id(), product.name(), product.description(), product.price()
                ));
            }
        }

        return new MenuResponse(availableProducts, totalFiltered);
    }
}
