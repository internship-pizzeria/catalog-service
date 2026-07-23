package com.pizzera.catalog_service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internal/products")
@RequiredArgsConstructor
class InternalProductController {

    private final ProductService productService;

    @PostMapping("/details")
    public List<InternalProductResponse> getProductDetails(@RequestBody List<Long> productIds) {
        return productService.getProductDetails(productIds);
    }
}
