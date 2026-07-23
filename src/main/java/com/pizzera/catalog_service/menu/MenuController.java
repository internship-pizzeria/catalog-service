package com.pizzera.catalog_service.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
class MenuController {

    private final MenuService menuService;

    @GetMapping
    public MenuResponse getMenu(@RequestParam Long locationId) {
        return menuService.getMenuForLocation(locationId);
    }
}
