package com.pizzera.catalog_service.location;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public record LocationPageResponse(
        List<LocationResponse> content,
        int pageNumber,
        int pageSize,
        long totalElements,
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
