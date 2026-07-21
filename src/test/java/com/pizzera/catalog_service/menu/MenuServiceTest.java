package com.pizzera.catalog_service.menu;

import com.pizzera.catalog_service.ingredient.Ingredient;
import com.pizzera.catalog_service.ingredient.LocationIngredientRepository;
import com.pizzera.catalog_service.location.LocationNotFoundException;
import com.pizzera.catalog_service.location.LocationRepository;
import com.pizzera.catalog_service.product.Product;
import com.pizzera.catalog_service.product.ProductIngredient;
import com.pizzera.catalog_service.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationIngredientRepository locationIngredientRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void shouldReturnAllProductsWhenNoIngredientsAreUnavailable() {
        // GIVEN
        Long locationId = 1L;
        when(locationRepository.existsById(locationId)).thenReturn(true);
        when(locationIngredientRepository.findUnavailableIngredientIds(locationId))
                .thenReturn(Collections.emptyList());

        Ingredient mozzarella = new Ingredient(1L, "Mozzarella", null);
        Ingredient sos = new Ingredient(2L, "Sos", null);

        Product pizza1 = new Product(1L, "Margherita", "Classic", new BigDecimal("25.00"), Instant.now(),
                List.of(createProductIngredient(mozzarella)));
        Product pizza2 = new Product(2L, "Pepperoni", "Spicy", new BigDecimal("30.00"), Instant.now(),
                List.of(createProductIngredient(mozzarella), createProductIngredient(sos)));

        when(productRepository.findAllWithIngredients()).thenReturn(List.of(pizza1, pizza2));

        // WHEN
        MenuResponse result = menuService.getMenuForLocation(locationId);

        // THEN
        assertEquals(2, result.products().size());
        assertEquals(0, result.totalFiltered());
    }

    @Test
    void shouldFilterProductsWithUnavailableIngredients() {
        // GIVEN
        Long locationId = 1L;
        when(locationRepository.existsById(locationId)).thenReturn(true);

        Ingredient mozzarella = new Ingredient(1L, "Mozzarella", null);
        Ingredient pepperoni = new Ingredient(2L, "Pepperoni", null);

        when(locationIngredientRepository.findUnavailableIngredientIds(locationId))
                .thenReturn(List.of(2L));

        Product margherita = new Product(1L, "Margherita", "Classic", new BigDecimal("25.00"), Instant.now(),
                List.of(createProductIngredient(mozzarella)));
        Product spicy = new Product(2L, "Pepperoni", "Spicy", new BigDecimal("30.00"), Instant.now(),
                List.of(createProductIngredient(mozzarella), createProductIngredient(pepperoni)));

        when(productRepository.findAllWithIngredients()).thenReturn(List.of(margherita, spicy));

        // WHEN
        MenuResponse result = menuService.getMenuForLocation(locationId);

        // THEN
        assertEquals(1, result.products().size());
        assertEquals("Margherita", result.products().getFirst().name());
        assertEquals(1, result.totalFiltered());
    }

    @Test
    void shouldReturnAllProductsWhenLocationHasNoAvailabilityData() {
        // GIVEN
        Long locationId = 1L;
        when(locationRepository.existsById(locationId)).thenReturn(true);
        when(locationIngredientRepository.findUnavailableIngredientIds(locationId))
                .thenReturn(Collections.emptyList());

        Product pizza = new Product(1L, "Margherita", "Classic", new BigDecimal("25.00"), Instant.now(),
                List.of(createProductIngredient(new Ingredient(1L, "Mozzarella", null))));

        when(productRepository.findAllWithIngredients()).thenReturn(List.of(pizza));

        // WHEN
        MenuResponse result = menuService.getMenuForLocation(locationId);

        // THEN
        assertEquals(1, result.products().size());
        assertEquals(0, result.totalFiltered());
    }

    @Test
    void shouldThrow404WhenLocationDoesNotExist() {
        // GIVEN
        Long locationId = 999L;
        when(locationRepository.existsById(locationId)).thenReturn(false);

        // WHEN & THEN
        LocationNotFoundException exception = assertThrows(LocationNotFoundException.class, () -> {
            menuService.getMenuForLocation(locationId);
        });

        assertEquals("Location with ID 999 not found", exception.getReason());
        verify(locationRepository, times(1)).existsById(locationId);
        verify(productRepository, never()).findAllWithIngredients();
    }

    private ProductIngredient createProductIngredient(Ingredient ingredient) {
        return new ProductIngredient(ingredient);
    }
}