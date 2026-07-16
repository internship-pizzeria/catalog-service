package com.pizzera.catalog_service.product;

import com.pizzera.catalog_service.location.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final Long LOCATION_ID = 100L;
    private static final Long PRODUCT_ID = 1L;
    private static final String CITY = "Zielona Góra";
    private static final String PRODUCT_NAME = "Margherita";
    private static final BigDecimal PRODUCT_PRICE = new BigDecimal("25.00");

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Location dummyLocation;

    @BeforeEach
    void setUp() {
        dummyLocation = new Location(CITY, "65-001", "Wyszyńskiego", "4", "PL", "Europe/Warsaw");
        ReflectionTestUtils.setField(dummyLocation, "id", LOCATION_ID);
    }

    @Test
    void shouldReturnProductWhenFoundByIdAndLocation() {
        // GIVEN
        Product dummyProduct = new Product(PRODUCT_NAME, "Sos, ser", PRODUCT_PRICE, dummyLocation);
        ReflectionTestUtils.setField(dummyProduct, "id", PRODUCT_ID);

        when(productRepository.findByIdAndLocationId(PRODUCT_ID, LOCATION_ID))
                .thenReturn(Optional.of(dummyProduct));

        // WHEN
        ProductResponse result = productService.getProductByIdAndLocation(PRODUCT_ID, LOCATION_ID);

        // THEN
        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.id());
        assertEquals(PRODUCT_NAME, result.name());
        assertEquals(PRODUCT_PRICE, result.price());
        assertEquals(LOCATION_ID, result.locationId());

        verify(productRepository, times(1)).findByIdAndLocationId(PRODUCT_ID, LOCATION_ID);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundByIdAndLocation() {
        // GIVEN
        Long nonExistentProductId = 99L;

        when(productRepository.findByIdAndLocationId(nonExistentProductId, LOCATION_ID))
                .thenReturn(Optional.empty());

        // WHEN
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductByIdAndLocation(nonExistentProductId, LOCATION_ID);
        });

        // THEN
        assertEquals("Product with ID: 99 not found.", exception.getMessage());
        verify(productRepository, times(1)).findByIdAndLocationId(nonExistentProductId, LOCATION_ID);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        // GIVEN
        String newProductName = "Pepperoni";
        BigDecimal newProductPrice = new BigDecimal("32.00");

        Product newProduct = new Product(newProductName, "Sos, ser, salami", newProductPrice, dummyLocation);

        Product savedProduct = new Product(newProductName, "Sos, ser, salami", newProductPrice, dummyLocation);
        ReflectionTestUtils.setField(savedProduct, "id", 2L);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // WHEN
        Product result = productService.createProduct(newProduct);

        // THEN
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(newProductName, result.getName());
        assertEquals(LOCATION_ID, result.getLocation().getId());

        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void shouldReturnInternalProductDetailsForValidIdsOnly() {
        // GIVEN
        List<Long> requestedIds = List.of(1L, 2L, 999L);

        Product pizza1 = new Product("Margherita", "Sos, ser", new BigDecimal("25.00"), dummyLocation);
        ReflectionTestUtils.setField(pizza1, "id", 1L);

        Product pizza2 = new Product("Pepperoni", "Sos, ser, salami", new BigDecimal("32.00"), dummyLocation);
        ReflectionTestUtils.setField(pizza2, "id", 2L);

        when(productRepository.findAllById(requestedIds)).thenReturn(List.of(pizza1, pizza2));

        // WHEN
        List<InternalProductResponse> result = productService.getProductDetails(requestedIds);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size(), "It should return only the two existing products");

        InternalProductResponse response1 = result.get(0);
        assertEquals(1L, response1.id());
        assertEquals("Margherita", response1.name());
        assertEquals(new BigDecimal("25.00"), response1.price());

        InternalProductResponse response2 = result.get(1);
        assertEquals(2L, response2.id());
        assertEquals("Pepperoni", response2.name());
        assertEquals(new BigDecimal("32.00"), response2.price());

        verify(productRepository, times(1)).findAllById(requestedIds);
    }
}