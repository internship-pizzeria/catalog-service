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
import org.springframework.test.util.ReflectionTestUtils;

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
        Location location = new Location(
                "Zielona Góra",
                "65-001",
                "Wyszyńskiego",
                "4",
                "PL",
                "Europe/Warsaw"
        );
        ReflectionTestUtils.setField(location, "id", 1L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Location> locationPage = new PageImpl<>(List.of(location));

        when(locationRepository.findByStatus(LocationStatus.ACTIVE, pageable)).thenReturn(locationPage);

        LocationPageResponse result = locationService.getAllActiveLocations(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.totalElements());

        LocationResponse response = result.content().get(0);
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
        Pageable pageable = PageRequest.of(0, 10);
        when(locationRepository.findByStatus(LocationStatus.ACTIVE, pageable)).thenReturn(Page.empty());

        LocationPageResponse result = locationService.getAllActiveLocations(null, pageable);

        assertNotNull(result);
        assertEquals(0, result.totalElements());
        verify(locationRepository, times(1)).findByStatus(LocationStatus.ACTIVE, pageable);
    }

    @Test
    void shouldFilterLocationsByCity() {
        Pageable pageable = PageRequest.of(0, 10);
        Location location = new Location(
                "Zielona Góra",
                "65-001",
                "Wyszyńskiego",
                "4",
                "PL",
                "Europe/Warsaw"
        );

        Page<Location> locationPage = new PageImpl<>(List.of(location));

        when(locationRepository.findByStatusAndCityContainingIgnoreCase(LocationStatus.ACTIVE, "Zielona", pageable))
                .thenReturn(locationPage);

        LocationPageResponse result = locationService.getAllActiveLocations("Zielona", pageable);

        assertNotNull(result);
        assertEquals(1, result.totalElements());
        assertEquals("Zielona Góra", result.content().get(0).city());

        verify(locationRepository, times(1))
                .findByStatusAndCityContainingIgnoreCase(LocationStatus.ACTIVE, "Zielona", pageable);

        verify(locationRepository, never()).findByStatus(any(), any());
    }
}