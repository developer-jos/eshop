package com.eshop.controller;

import com.eshop.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    @BeforeEach
    void setUp() {
        // Set up MockMvc in standalone mode (without starting the server)
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
    }

    @Test
    void testGetPlaceSuggestions_Success() {
        // Arrange
        String input = "Kasi";
        double latitude = 19.868160;
        double longitude = 82.782448;
        int radius = 500000;

        List<String> suggestions = List.of("Kasi", "Kashi");

        when(locationService.getPlaceSuggestions(input, latitude, longitude, radius)).thenReturn(suggestions);

        // Act
        ResponseEntity<List<String>> responseEntity =
                locationController.getPlaceSuggestions(input, latitude, longitude, radius);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(suggestions, responseEntity.getBody());

        verify(locationService, times(1)).getPlaceSuggestions(input, latitude, longitude, radius);
    }

    @Test
    void testGetPlaceSuggestions_NoSuggestions() {
        // Arrange
        String input = "Unknown";
        double latitude = 0.0;
        double longitude = 0.0;
        int radius = 500000;

        when(locationService.getPlaceSuggestions(input, latitude, longitude, radius)).thenReturn(List.of());

        // Act
        ResponseEntity<List<String>> responseEntity =
                locationController.getPlaceSuggestions(input, latitude, longitude, radius);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(0, responseEntity.getBody().size());

        verify(locationService, times(1)).getPlaceSuggestions(input, latitude, longitude, radius);
    }

    @Test
    void testGetPlaceSuggestions_ServiceThrowsException() {
        // Arrange
        String input = "Invalid";
        double latitude = 0.0;
        double longitude = 0.0;
        int radius = 500000;

        when(locationService.getPlaceSuggestions(input, latitude, longitude, radius)).thenThrow(
                new RuntimeException("Service error"));

        // Act & Assert
        try {
            locationController.getPlaceSuggestions(input, latitude, longitude, radius);
        } catch(RuntimeException e) {
            assertEquals("Service error", e.getMessage());
        }

        verify(locationService, times(1)).getPlaceSuggestions(input, latitude, longitude, radius);
    }
}
