package com.pizzera.catalog_service.menu;

import com.pizzera.catalog_service.location.LocationNotFoundException;
import com.pizzera.catalog_service.location.LocationRepository;
import com.pizzera.catalog_service.product.Product;
import com.pizzera.catalog_service.product.ProductRepository;
import com.pizzera.catalog_service.product.ProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(locationRepository.existsById(locationId)).thenReturn(true);

        Product pizza1 = new Product(1L, "Margherita", "Classic", new BigDecimal("25.00"), Instant.now());
        Product pizza2 = new Product(2L, "Pepperoni", "Spicy", new BigDecimal("30.00"), Instant.now());
        when(productRepository.findAll()).thenReturn(List.of(pizza1, pizza2));

        // WHEN
        List<ProductResponse> result = menuService.getMenuForLocation(locationId);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Margherita", result.get(0).name());
        assertEquals("Pepperoni", result.get(1).name());
        assertEquals(new BigDecimal("25.00"), result.get(0).price());

        verify(locationRepository, times(1)).existsById(locationId);
        verify(productRepository, times(1)).findAll();
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
        assertEquals("Location with ID 999 not found", exception.getReason());

        verify(locationRepository, times(1)).existsById(locationId);
        verify(productRepository, never()).findAll();
    }
}
