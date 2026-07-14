package com.pizzera.catalog_service.location;

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
        // GIVEN
        Location location = new Location();
        location.setId(1L);
        location.setCity("Zielona Góra");
        location.setPostalCode("65-001");
        location.setStreet("Wyszyńskiego");
        location.setBuildingNumber("4");
        location.setCountryCode("PL");
        location.setTimezone("Europe/Warsaw");
        location.setStatus(LocationStatus.ACTIVE);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Location> locationPage = new PageImpl<>(List.of(location));

        when(locationRepository.findByStatus(LocationStatus.ACTIVE, pageable)).thenReturn(locationPage);

        // WHEN
        Page<LocationResponse> result = locationService.getAllActiveLocations(null, pageable);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        LocationResponse response = result.getContent().get(0);
        assertEquals(1L, response.id());
        assertEquals("Zielona Góra", response.city());
        assertEquals("65-001", response.postalCode());
        assertEquals("Wyszyńskiego", response.street());
        assertEquals("4", response.buildingNumber());
        assertEquals("PL", response.countryCode());
        assertEquals("Europe/Warsaw", response.timezone());
        assertEquals(LocationStatus.ACTIVE, response.status());

        verify(locationRepository, times(1)).findByStatus(LocationStatus.ACTIVE, pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoActiveLocations() {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        when(locationRepository.findByStatus(LocationStatus.ACTIVE, pageable)).thenReturn(Page.empty());

        // WHEN
        Page<LocationResponse> result = locationService.getAllActiveLocations(null, pageable);

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(locationRepository, times(1)).findByStatus(LocationStatus.ACTIVE, pageable);
    }

    @Test
    void shouldFilterLocationsByCity() {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        Location location = new Location();
        location.setId(1L);
        location.setCity("Zielona Góra");
        location.setPostalCode("65-001");
        location.setStreet("Wyszyńskiego");
        location.setBuildingNumber("4");
        location.setCountryCode("PL");
        location.setTimezone("Europe/Warsaw");
        location.setStatus(LocationStatus.ACTIVE);

        Page<Location> locationPage = new PageImpl<>(List.of(location));

        when(locationRepository.findByStatusAndCityContainingIgnoreCase(LocationStatus.ACTIVE, "Zielona", pageable))
                .thenReturn(locationPage);

        // WHEN
        Page<LocationResponse> result = locationService.getAllActiveLocations("Zielona", pageable);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Zielona Góra", result.getContent().get(0).city());

        verify(locationRepository, times(1))
                .findByStatusAndCityContainingIgnoreCase(LocationStatus.ACTIVE, "Zielona", pageable);

        verify(locationRepository, never()).findByStatus(any(), any());
    }
}