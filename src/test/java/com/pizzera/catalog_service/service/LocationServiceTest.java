package com.pizzera.catalog_service.service;

import com.pizzera.catalog_service.dto.LocationResponse;
import com.pizzera.catalog_service.entity.Location;
import com.pizzera.catalog_service.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    void shouldReturnPageOfActiveLocations() {
        Location location = new Location();
        location.setId(1L);
        location.setCity("Zielona Góra");
        location.setStreet("Wyszyńskiego");
        location.setBuildingNumber("4");
        location.setCountryCode("PL");
        location.setActive(true);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Location> locationPage = new PageImpl<>(List.of(location));

        when(locationRepository.findByIsActiveTrue(pageable)).thenReturn(locationPage);

        Page<LocationResponse> result = locationService.getAllActiveLocations(pageable);


        assertNotNull(result);
        assertEquals(1, result.getTotalElements());


        LocationResponse response = result.getContent().get(0);
        assertEquals(1L, response.id());
        assertEquals("Zielona Góra", response.city());
        assertEquals("Wyszyńskiego", response.street());
        assertEquals("4", response.buildingNumber());
        assertEquals("PL", response.countryCode());

        verify(locationRepository, times(1)).findByIsActiveTrue(pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoActiveLocations() {
        Pageable pageable = PageRequest.of(0, 10);
        when(locationRepository.findByIsActiveTrue(pageable)).thenReturn(Page.empty());

        Page<LocationResponse> result = locationService.getAllActiveLocations(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(locationRepository, times(1)).findByIsActiveTrue(pageable);
    }
}
