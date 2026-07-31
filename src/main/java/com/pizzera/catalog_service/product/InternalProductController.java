package com.pizzera.catalog_service.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internal/products")
@RequiredArgsConstructor
@Tag(name = "Internal Products", description = "Internal product endpoints for service-to-service communication (requires LocationId and X-User-Id headers)")
class InternalProductController {

    private final ProductService productService;

    @PostMapping("/details")
    @Operation(summary = "Batch fetch product details", description = "Returns product information (id, name, price, availability) for a list of product IDs at a given location. Non-existing product IDs are silently skipped.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product details for the requested IDs"),
            @ApiResponse(responseCode = "400", description = "Missing LocationId header, invalid header value, or missing locationId query parameter"),
            @ApiResponse(responseCode = "401", description = "Missing LocationId or X-User-Id header")
    })
    public List<InternalProductResponse> getProductDetails(
            @Parameter(description = "List of product IDs to fetch", example = "[1,2,3]") @RequestBody List<Long> productIds,
            @Parameter(description = "Location ID to evaluate product availability for", example = "1", required = true) @RequestParam Long locationId,
            @Parameter(description = "Authenticated location ID", example = "1", required = true, in = ParameterIn.HEADER) @RequestHeader("LocationId") String locationIdHeader,
            @Parameter(description = "Authenticated user ID", example = "1", required = true, in = ParameterIn.HEADER) @RequestHeader("X-User-Id") String userIdHeader) {
        return productService.getProductDetails(productIds, locationId);
    }

}
