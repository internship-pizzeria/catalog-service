package com.pizzera.catalog_service.product;

import com.pizzera.catalog_service.security.LocationContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internal/products")
@RequiredArgsConstructor
@Tag(name = "Internal Products", description = "Internal product endpoints for service-to-service communication (requires LocationId and X-User-Id headers)")
class InternalProductController {

    private final ProductService productService;
    private final LocationContext locationContext;

    @PostMapping("/details")
    @Operation(summary = "Batch fetch product details", description = "Returns basic product information (id, name, price) for a list of product IDs")
    public List<InternalProductResponse> getProductDetails(
            @Parameter(description = "List of product IDs to fetch") @RequestBody List<Long> productIds,
            @Parameter(description = "Authenticated location ID", example = "1", required = true, in = ParameterIn.HEADER) @RequestHeader("LocationId") String locationIdHeader,
            @Parameter(description = "Authenticated user ID", example = "1", required = true, in = ParameterIn.HEADER) @RequestHeader("X-User-Id") String userIdHeader) {
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
