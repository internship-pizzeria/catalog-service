package com.pizzera.catalog_service.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class LocationContext {

    public Long getCurrentLocationId() {
        return getAttribute("locationId");
    }

    public Long getCurrentUserId() {
        return getAttribute("userId");
    }

    private Long getAttribute(String name) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        Object value = request.getAttribute(name);
        if (value instanceof Long) {
            return (Long) value;
        }
        return null;
    }
}