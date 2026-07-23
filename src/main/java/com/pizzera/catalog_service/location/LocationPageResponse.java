package com.pizzera.catalog_service.location;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Schema(description = "Paginated list of restaurant locations")
public record LocationPageResponse(
        @Schema(description = "List of locations on this page")
        List<LocationResponse> content,
        @Schema(description = "Current page number (0-based)", example = "0")
        int pageNumber,
        @Schema(description = "Number of items per page", example = "20")
        int pageSize,
        @Schema(description = "Total number of matching locations", example = "3")
        long totalElements,
        @Schema(description = "Total number of pages", example = "1")
        int totalPages
) implements Serializable {

    public static LocationPageResponse from(Page<LocationResponse> page) {
        return new LocationPageResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
