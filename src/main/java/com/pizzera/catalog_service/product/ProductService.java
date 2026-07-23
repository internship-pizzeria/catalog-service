package com.pizzera.catalog_service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new ProductNotFoundException(id));
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
