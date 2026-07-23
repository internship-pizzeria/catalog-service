package com.pizzera.catalog_service.ingredient;

import com.pizzera.catalog_service.ingredient.Ingredient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private LocationIngredientRepository locationIngredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    @Test
    void shouldReturnAllIngredientsWithDefaultAvailability() {
        // GIVEN
        Long locationId = 1L;

        Ingredient mozzarella = new Ingredient(1L, "Mozzarella", null);
        Ingredient pepperoni = new Ingredient(2L, "Pepperoni", null);

        when(ingredientRepository.findAll()).thenReturn(List.of(mozzarella, pepperoni));
        when(locationIngredientRepository.findByLocationId(locationId)).thenReturn(List.of());

        // WHEN
        List<LocationIngredientResponse> result = ingredientService.getAvailabilityForLocation(locationId);

        // THEN
        assertEquals(2, result.size());
        assertTrue(result.get(0).available());
        assertTrue(result.get(1).available());
        assertEquals("Mozzarella", result.get(0).name());
        assertEquals("Pepperoni", result.get(1).name());
    }

    @Test
    void shouldReturnIngredientsWithCorrectAvailability() {
        // GIVEN
        Long locationId = 1L;

        Ingredient mozzarella = new Ingredient(1L, "Mozzarella", null);
        Ingredient pepperoni = new Ingredient(2L, "Pepperoni", null);

        LocationIngredient unavailable = new LocationIngredient(locationId, mozzarella, false);

        when(ingredientRepository.findAll()).thenReturn(List.of(mozzarella, pepperoni));
        when(locationIngredientRepository.findByLocationId(locationId)).thenReturn(List.of(unavailable));

        // WHEN
        List<LocationIngredientResponse> result = ingredientService.getAvailabilityForLocation(locationId);

        // THEN
        assertEquals(2, result.size());
        assertFalse(result.get(0).available());
        assertTrue(result.get(1).available());
    }

    @Test
    void shouldToggleFromAvailableToUnavailable() {
        // GIVEN
        Long locationId = 1L;
        Long ingredientId = 1L;

        Ingredient mozzarella = new Ingredient(ingredientId, "Mozzarella", null);
        LocationIngredient existing = new LocationIngredient(locationId, mozzarella, true);

        when(locationIngredientRepository.findByLocationIdAndIngredientId(locationId, ingredientId))
                .thenReturn(Optional.of(existing));
        when(locationIngredientRepository.save(any(LocationIngredient.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        LocationIngredientResponse result = ingredientService.toggleAvailability(locationId, ingredientId);

        // THEN
        assertFalse(result.available());
        assertEquals("Mozzarella", result.name());
        verify(locationIngredientRepository).save(any(LocationIngredient.class));
        verify(ingredientRepository, never()).findById(any());
    }

    @Test
    void shouldToggleFromUnavailableToAvailable() {
        // GIVEN
        Long locationId = 1L;
        Long ingredientId = 1L;

        Ingredient mozzarella = new Ingredient(ingredientId, "Mozzarella", null);
        LocationIngredient existing = new LocationIngredient(locationId, mozzarella, false);

        when(locationIngredientRepository.findByLocationIdAndIngredientId(locationId, ingredientId))
                .thenReturn(Optional.of(existing));
        when(locationIngredientRepository.save(any(LocationIngredient.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        LocationIngredientResponse result = ingredientService.toggleAvailability(locationId, ingredientId);

        // THEN
        assertTrue(result.available());
    }

    @Test
    void shouldCreateNewEntryWhenNotExists() {
        // GIVEN
        Long locationId = 1L;
        Long ingredientId = 1L;

        Ingredient mozzarella = new Ingredient(ingredientId, "Mozzarella", null);

        when(locationIngredientRepository.findByLocationIdAndIngredientId(locationId, ingredientId))
                .thenReturn(Optional.empty());
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(mozzarella));
        when(locationIngredientRepository.save(any(LocationIngredient.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        LocationIngredientResponse result = ingredientService.toggleAvailability(locationId, ingredientId);

        // THEN
        assertFalse(result.available());
        verify(ingredientRepository).findById(ingredientId);
        verify(locationIngredientRepository).save(any(LocationIngredient.class));
    }

    @Test
    void shouldThrowExceptionWhenIngredientNotFound() {
        // GIVEN
        Long locationId = 1L;
        Long nonExistentIngredientId = 99L;

        when(locationIngredientRepository.findByLocationIdAndIngredientId(locationId, nonExistentIngredientId))
                .thenReturn(Optional.empty());
        when(ingredientRepository.findById(nonExistentIngredientId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(IngredientNotFoundException.class, () ->
                ingredientService.toggleAvailability(locationId, nonExistentIngredientId));
    }
}