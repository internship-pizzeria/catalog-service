package com.pizzera.catalog_service.menu;

import com.pizzera.catalog_service.location.LocationNotFoundException;
import com.pizzera.catalog_service.location.LocationRepository;
import com.pizzera.catalog_service.product.ProductRepository;
import com.pizzera.catalog_service.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "menu", key = "#locationId")
    public List<ProductResponse> getMenuForLocation(Long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new LocationNotFoundException(locationId);
        }
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::new)
                .toList();
    }
}
