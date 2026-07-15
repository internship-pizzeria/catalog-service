package com.pizzera.catalog_service.product;

import com.pizzera.catalog_service.location.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnProductWhenFoundByIdAndLocation() {
        // GIVEN
        Long productId = 1L;
        Long locationId = 100L;

        Location dummyLocation = new Location("Zielona Góra", "65-001", "Wyszyńskiego", "4", "PL", "Europe/Warsaw");
        ReflectionTestUtils.setField(dummyLocation, "id", locationId);

        Product dummyProduct = new Product("Margherita", "Sos, ser", new BigDecimal("25.00"), dummyLocation);
        ReflectionTestUtils.setField(dummyProduct, "id", productId);

        when(productRepository.findByIdAndLocationId(productId, locationId))
                .thenReturn(Optional.of(dummyProduct));

        // WHEN
        ProductResponse result = productService.getProductByIdAndLocation(productId, locationId);

        // THEN
        assertNotNull(result);
        assertEquals(productId, result.id());
        assertEquals("Margherita", result.name());
        assertEquals(new BigDecimal("25.00"), result.price());
        assertEquals(locationId, result.locationId());

        verify(productRepository, times(1)).findByIdAndLocationId(productId, locationId);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundByIdAndLocation() {
        // GIVEN
        Long productId = 99L;
        Long locationId = 100L;

        when(productRepository.findByIdAndLocationId(productId, locationId))
                .thenReturn(Optional.empty());

        // WHEN
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductByIdAndLocation(productId, locationId);
        });
        // THEN
        assertEquals("Product with ID: 99 not found.", exception.getMessage());
        verify(productRepository, times(1)).findByIdAndLocationId(productId, locationId);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        // GIVEN
        Location dummyLocation = new Location("Warszawa", "00-001", "Westerplatte", "12", "PL", "Europe/Warsaw");
        ReflectionTestUtils.setField(dummyLocation, "id", 2L);

        Product newProduct = new Product("Pepperoni", "Sos, ser, salami", new BigDecimal("32.00"), dummyLocation);

        Product savedProduct = new Product("Pepperoni", "Sos, ser, salami", new BigDecimal("32.00"), dummyLocation);
        ReflectionTestUtils.setField(savedProduct, "id", 1L);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // WHEN
        Product result = productService.createProduct(newProduct);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pepperoni", result.getName());

        verify(productRepository, times(1)).save(newProduct);
    }
}