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
                .map(this::toResponse)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }


    @CacheEvict(value = "menu", allEntries = true)
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product(request.name(), request.description(), request.price());
        Product saved = productRepository.save(product);
        return toResponse(saved);
    }


    @Transactional(readOnly = true)
    public List<ProductWithIngredientsResponse> findAllWithIngredients() {
        return productRepository.findAllWithIngredients().stream()
                .map(this::toResponseWithIngredients)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InternalProductResponse> getProductDetails(List<Long> productIds) {
        return productRepository.findAllById(productIds)
                .stream()
                .map(this::toInternalResponse)
                .toList();
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(), product.getName(), product.getDescription(), product.getPrice()
        );
    }

    private InternalProductResponse toInternalResponse(Product product) {
        return new InternalProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    private ProductWithIngredientsResponse toResponseWithIngredients(Product product) {
        return new ProductWithIngredientsResponse(
                product.getId(), product.getName(), product.getDescription(), product.getPrice(),
                product.getIngredients().stream()
                        .map(pi -> pi.getIngredient().getId())
                        .toList()
        );
    }
}
