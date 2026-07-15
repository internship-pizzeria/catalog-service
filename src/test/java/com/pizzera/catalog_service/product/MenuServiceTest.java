package com.pizzera.catalog_service.product;

import com.pizzera.catalog_service.location.Location;
import com.pizzera.catalog_service.location.LocationNotFoundException;
import com.pizzera.catalog_service.location.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void shouldReturnMenuForExistingLocation() {
        // GIVEN
        Long locationId = 1L;
        when (locationRepository.existsById(locationId)).thenReturn(true);

        Location dummyLocation = new Location("Zielona Góra", "65-001", "Wyszyńskiego", "4", "PL", "Europe/Warsaw");

        Product pizza1 = new Product(1L, "Margherita", "Classic", new BigDecimal("25.00"), dummyLocation, Instant.now());
        Product pizza2 = new Product(2L, "Pepperoni", "Spicy", new BigDecimal("30.00"), dummyLocation, Instant.now());
        when(productRepository.findByLocationId(locationId)).thenReturn(List.of(pizza1, pizza2));

        // WHEN
        List<ProductResponse> result = menuService.getMenuForLocation(locationId);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Margherita", result.get(0).name());
        assertEquals("Pepperoni", result.get(1).name());
        assertEquals(new BigDecimal("25.00"), result.get(0).price());

        verify(locationRepository, times(1)).existsById(locationId);
        verify(productRepository, times(1)).findByLocationId(locationId);
    }

    @Test
    void shouldThrow404WhenLocationDoesNotExist() {
        // GIVEN
        Long locationId = 999L;
        when(locationRepository.existsById(locationId)).thenReturn(false);

        // WHEN
        LocationNotFoundException exception = assertThrows(LocationNotFoundException.class, () -> {
            menuService.getMenuForLocation(locationId);
        });

        // THEN
        assertEquals("Location with ID 999 not found", exception.getMessage());

        verify(locationRepository, times(1)).existsById(locationId);

        verify(productRepository, never()).findByLocationId(any());
    }

}
