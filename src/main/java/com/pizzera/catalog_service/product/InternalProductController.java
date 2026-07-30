package com.pizzera.catalog_service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internal/products")
@RequiredArgsConstructor
class InternalProductController {

    private final ProductService productService;

    @PostMapping("/details")
    public List<InternalProductResponse> getProductDetails(
            @RequestBody List<Long> productIds,
            @RequestParam Long locationId) {
        return productService.getProductDetails(productIds, locationId);
    }

}
