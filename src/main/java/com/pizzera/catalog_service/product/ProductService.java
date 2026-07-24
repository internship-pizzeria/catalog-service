package com.pizzera.catalog_service.product;

import com.pizzera.catalog_service.ingredient.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final IngredientService ingredientService;

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id, Long locationId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        boolean available = true;
        if (locationId != null) {
            Set<Long> unavailableIds = ingredientService.findUnavailableIngredientIds(locationId);
            boolean hasUnavailable = product.getIngredients().stream()
                    .anyMatch(pi -> unavailableIds.contains(pi.getIngredient().getId()));
            available = !hasUnavailable;
        }

        return ProductResponse.from(product, available);
    }


    @CacheEvict(value = "menu", allEntries = true)
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product(request.name(), request.description(), request.price());
        return ProductResponse.from(productRepository.save(product));
    }


    @Transactional(readOnly = true)
    public List<ProductWithIngredientsResponse> findAllWithIngredients() {
        return productRepository.findAllWithIngredients().stream()
                .map(ProductWithIngredientsResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InternalProductResponse> getProductDetails(List<Long> productIds) {
        return productRepository.findAllById(productIds)
                .stream()
                .map(InternalProductResponse::from)
                .toList();
    }
}
