package com.pizzera.catalog_service.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class LocationAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();

        if (path.startsWith("/api/v1/internal/")) {
            String locationIdHeader = request.getHeader("LocationId");
            String userIdHeader = request.getHeader("X-User-Id");

            if (locationIdHeader == null || locationIdHeader.isBlank()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Missing LocationId header\"}");
                return;
            }

            if (userIdHeader == null || userIdHeader.isBlank()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Missing X-User-Id header\"}");
                return;
            }

            try {
                Long locationId = Long.parseLong(locationIdHeader);
                Long userId = Long.parseLong(userIdHeader);
                request.setAttribute("locationId", locationId);
                request.setAttribute("userId", userId);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid header value\"}");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}