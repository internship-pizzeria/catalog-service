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
    // GIVEN
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

        // WHEN
        Page<LocationResponse> result = locationService.getAllActiveLocations(null, pageable);

        // THEN
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
        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        when(locationRepository.findByIsActiveTrue(pageable)).thenReturn(Page.empty());

        // WHEN
        Page<LocationResponse> result = locationService.getAllActiveLocations(null, pageable);

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(locationRepository, times(1)).findByIsActiveTrue(pageable);
    }

    @Test
    void shouldFilterLocationsByCity() {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        Location location = new Location();
        location.setId(1L);
        location.setCity("Zielona Góra");
        location.setStreet("Wyszyńskiego");
        location.setBuildingNumber("4");
        location.setCountryCode("PL");
        location.setActive(true);

        Page<Location> locationPage = new PageImpl<>(List.of(location));

        when(locationRepository.findByIsActiveTrueAndCityContainingIgnoreCase("Zielona", pageable))
                .thenReturn(locationPage);

        // WHEN
        Page<LocationResponse> result = locationService.getAllActiveLocations("Zielona", pageable);


        // THEN
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Zielona Góra", result.getContent().get(0).city());

        verify(locationRepository, times(1)).findByIsActiveTrueAndCityContainingIgnoreCase("Zielona", pageable);
        verify(locationRepository, never()).findByIsActiveTrue(any());
    }


}
