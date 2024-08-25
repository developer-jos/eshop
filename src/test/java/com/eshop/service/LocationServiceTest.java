package com.eshop.service;

import com.eshop.exception.EshopException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LocationService locationService;

    @Test
    void testGetPlaceSuggestions_Success() {
        // Arrange
        String input = "Kasi";
        double latitude = 19.868160;
        double longitude = 82.782448;
        int radius = 500000;

        // Mocking the response from the API
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");

        Map<String, Object> structuredFormatting = new HashMap<>();
        structuredFormatting.put("main_text", "Kasi");

        Map<String, Object> prediction = new HashMap<>();
        prediction.put("structured_formatting", structuredFormatting);

        response.put("predictions", List.of(prediction));

        // Mock RestTemplate behavior
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        // Act
        List<String> result = locationService.getPlaceSuggestions(input, latitude, longitude, radius);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Kasi", result.get(0));

        // Verify that RestTemplate was called once
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Map.class));
    }

    @Test
    void testGetPlaceSuggestions_LocationNotFound() {
        // Arrange
        String input = "Unknown";
        double latitude = 0.0;
        double longitude = 0.0;
        int radius = 500000;

        // Mocking the response from the API
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ZERO_RESULTS");

        // Mock RestTemplate behavior
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        // Act & Assert
        EshopException exception = assertThrows(EshopException.class,
                                                () -> locationService.getPlaceSuggestions(input, latitude, longitude,
                                                                                          radius));

        assertEquals("Location not found", exception.getMessage());

        // Verify that RestTemplate was called once
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Map.class));
    }

    @Test
    void testGetPlaceSuggestions_NullResponse() {
        // Arrange
        String input = "Unknown";
        double latitude = 0.0;
        double longitude = 0.0;
        int radius = 500000;

        // Mock RestTemplate to return null
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);

        // Act & Assert
        EshopException exception = assertThrows(EshopException.class,
                                                () -> locationService.getPlaceSuggestions(input, latitude, longitude,
                                                                                          radius));

        assertEquals("Location not found", exception.getMessage());

        // Verify that RestTemplate was called once
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Map.class));
    }


    @Test
    void testGetPlaceSuggestions_EmptyPredictions() {
        // Arrange
        String input = "Kasi";
        double latitude = 19.868160;
        double longitude = 82.782448;
        int radius = 500000;

        // Mocking the response from the API with empty predictions
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("predictions", Collections.emptyList());

        // Mock RestTemplate behavior
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(response);

        // Act
        List<String> result = locationService.getPlaceSuggestions(input, latitude, longitude, radius);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify that RestTemplate was called once
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Map.class));
    }

}
