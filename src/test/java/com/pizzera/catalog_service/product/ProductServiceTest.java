package com.pizzera.catalog_service.product;

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

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnProductWhenFoundById() {
        // GIVEN
        Long productId = 1L;
        String productName = "Margherita";
        BigDecimal productPrice = new BigDecimal("25.00");

        Product dummyProduct = new Product(productName, "Sos, ser", productPrice);
        ReflectionTestUtils.setField(dummyProduct, "id", productId);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(dummyProduct));

        // WHEN
        ProductResponse result = productService.getProductById(productId);

        // THEN
        assertNotNull(result);
        assertEquals(productId, result.id());
        assertEquals(productName, result.name());
        assertEquals(productPrice, result.price());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundById() {
        // GIVEN
        Long nonExistentProductId = 99L;

        when(productRepository.findById(nonExistentProductId))
                .thenReturn(Optional.empty());

        // WHEN
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(nonExistentProductId);
        });

        // THEN
        assertEquals("Product with ID: " + nonExistentProductId + " not found.", exception.getReason());
        verify(productRepository, times(1)).findById(nonExistentProductId);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        // GIVEN
        Long savedProductId = 2L;
        String newProductName = "Pepperoni";
        BigDecimal newProductPrice = new BigDecimal("32.00");

        Product newProduct = new Product(newProductName, "Sos, ser, salami", newProductPrice);

        Product savedProduct = new Product(newProductName, "Sos, ser, salami", newProductPrice);
        ReflectionTestUtils.setField(savedProduct, "id", savedProductId);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // WHEN
        Product result = productService.createProduct(newProduct);

        // THEN
        assertNotNull(result);
        assertEquals(savedProductId, result.getId());
        assertEquals(newProductName, result.getName());

        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void shouldReturnInternalProductDetailsForValidIdsOnly() {
        // GIVEN
        Long pizzaId1 = 1L;
        String pizzaName1 = "Margherita";
        BigDecimal pizzaPrice1 = new BigDecimal("25.00");

        Long pizzaId2 = 2L;
        String pizzaName2 = "Pepperoni";
        BigDecimal pizzaPrice2 = new BigDecimal("32.00");

        Long nonExistentId = 999L;

        List<Long> requestedIds = List.of(pizzaId1, pizzaId2, nonExistentId);

        Product pizza1 = new Product(pizzaName1, "Sos, ser", pizzaPrice1);
        ReflectionTestUtils.setField(pizza1, "id", pizzaId1);

        Product pizza2 = new Product(pizzaName2, "Sos, ser, salami", pizzaPrice2);
        ReflectionTestUtils.setField(pizza2, "id", pizzaId2);

        when(productRepository.findAllById(requestedIds)).thenReturn(List.of(pizza1, pizza2));

        // WHEN
        List<InternalProductResponse> result = productService.getProductDetails(requestedIds);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size(), "It should return only the two existing products");

        InternalProductResponse response1 = result.get(0);
        assertEquals(pizzaId1, response1.id());
        assertEquals(pizzaName1, response1.name());
        assertEquals(pizzaPrice1, response1.price());

        InternalProductResponse response2 = result.get(1);
        assertEquals(pizzaId2, response2.id());
        assertEquals(pizzaName2, response2.name());
        assertEquals(pizzaPrice2, response2.price());

        verify(productRepository, times(1)).findAllById(requestedIds);
    }
}