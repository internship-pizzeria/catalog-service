package com.pizzera.catalog_service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductResponse getProductByIdAndLocation(Long id, Long locationId) {
        return productRepository.findByIdAndLocationId(id, locationId)
                .map(ProductResponse::new)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}
