package com.pizzera.catalog_service.product;

import com.pizzera.catalog_service.security.LocationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internal/products")
@RequiredArgsConstructor
class InternalProductController {

    private final ProductService productService;
    private final LocationContext locationContext;

    @PostMapping("/details")
    public List<InternalProductResponse> getProductDetails(@RequestBody List<Long> productIds) {
        validateAccess();
        return productService.getProductDetails(productIds);
    }

    private void validateAccess() {
        Long currentLocationId = locationContext.getCurrentLocationId();
        if (currentLocationId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing location context");
        }
    }
}
