package com.pizzera.catalog_service.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class LocationContext {

    public Long getCurrentLocationId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        Object locationId = request.getAttribute("locationId");
        if (locationId instanceof Long) {
            return (Long) locationId;
        }
        return null;
    }
}