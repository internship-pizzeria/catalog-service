package com.pizzera.catalog_service.menu;

import com.pizzera.catalog_service.ingredient.IngredientService;
import com.pizzera.catalog_service.location.LocationNotFoundException;
import com.pizzera.catalog_service.location.LocationService;
import com.pizzera.catalog_service.product.ProductService;
import com.pizzera.catalog_service.product.ProductResponse;
import com.pizzera.catalog_service.product.ProductWithIngredientsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private LocationService locationService;

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private MenuService menuService;

    @Test
    void shouldReturnAllProductsWhenNoIngredientsAreUnavailable() {
        // GIVEN
        Long locationId = 1L;

        ProductWithIngredientsResponse pizza1 = new ProductWithIngredientsResponse(
                1L, "Margherita", "Classic", new BigDecimal("25.00"), List.of(1L));
        ProductWithIngredientsResponse pizza2 = new ProductWithIngredientsResponse(
                2L, "Pepperoni", "Spicy", new BigDecimal("30.00"), List.of(1L, 2L));

        when(productService.findAllWithIngredients()).thenReturn(List.of(pizza1, pizza2));
        when(ingredientService.findUnavailableIngredientIds(locationId))
                .thenReturn(Collections.emptySet());

        // WHEN
        MenuResponse result = menuService.getMenuForLocation(locationId);

        // THEN
        assertEquals(2, result.products().size());
        assertEquals(0, result.unavailableCount());
        assertTrue(result.products().stream().allMatch(ProductResponse::available));
    }

    @Test
    void shouldFilterProductsWithUnavailableIngredients() {
        // GIVEN
        Long locationId = 1L;

        ProductWithIngredientsResponse margherita = new ProductWithIngredientsResponse(
                1L, "Margherita", "Classic", new BigDecimal("25.00"), List.of(1L));
        ProductWithIngredientsResponse spicy = new ProductWithIngredientsResponse(
                2L, "Pepperoni", "Spicy", new BigDecimal("30.00"), List.of(1L, 2L));

        when(productService.findAllWithIngredients()).thenReturn(List.of(margherita, spicy));
        when(ingredientService.findUnavailableIngredientIds(locationId))
                .thenReturn(Set.of(2L));

        // WHEN
        MenuResponse result = menuService.getMenuForLocation(locationId);

        // THEN
        assertEquals(2, result.products().size());
        assertEquals("Margherita", result.products().getFirst().name());
        assertTrue(result.products().getFirst().available());
        assertEquals("Pepperoni", result.products().get(1).name());
        assertFalse(result.products().get(1).available());
        assertEquals(1, result.unavailableCount());
    }

    @Test
    void shouldReturnAllProductsWhenLocationHasNoAvailabilityData() {
        // GIVEN
        Long locationId = 1L;

        ProductWithIngredientsResponse pizza = new ProductWithIngredientsResponse(
                1L, "Margherita", "Classic", new BigDecimal("25.00"), List.of(1L));

        when(productService.findAllWithIngredients()).thenReturn(List.of(pizza));
        when(ingredientService.findUnavailableIngredientIds(locationId))
                .thenReturn(Collections.emptySet());

        // WHEN
        MenuResponse result = menuService.getMenuForLocation(locationId);

        // THEN
        assertEquals(1, result.products().size());
        assertEquals(0, result.unavailableCount());
        assertTrue(result.products().getFirst().available());
    }

    @Test
    void shouldThrow404WhenLocationDoesNotExist() {
        // GIVEN
        Long locationId = 999L;
        doThrow(new LocationNotFoundException(locationId))
                .when(locationService).ensureExists(locationId);

        // WHEN & THEN
        LocationNotFoundException exception = assertThrows(LocationNotFoundException.class, () -> {
            menuService.getMenuForLocation(locationId);
        });

        assertEquals("Location with ID 999 not found", exception.getReason());
        verify(productService, never()).findAllWithIngredients();
    }
}
