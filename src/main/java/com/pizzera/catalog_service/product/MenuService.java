package com.pizzera.catalog_service.product;

import com.pizzera.catalog_service.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class MenuService {

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> getMenuForLocation(Long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with ID " + locationId + " not found");
        }
        return productRepository.findByLocationId(locationId)
                .stream()
                .map(ProductResponse::new)
                .toList();
    }
}
