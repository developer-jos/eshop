package com.eshop.service;

import com.eshop.exception.EshopException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for handling location-based operations.
 */
@Service
@RequiredArgsConstructor
public class LocationService {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    /**
     * Retrieves place suggestions based on the input and location.
     *
     * @param input the search input
     * @param latitude the latitude of the location
     * @param longitude the longitude of the location
     * @param radius the search radius in meters
     * @return a list of suggested place names
     * @exception EshopException if the location is not found or the API response is invalid
     */
    public List<String> getPlaceSuggestions(String input, double latitude, double longitude, int radius) {

        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/autocomplete/json")
                .queryParam("input", input).queryParam("location", latitude+","+longitude).queryParam("radius", radius)
                .queryParam("key", apiKey).toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if(response == null || !"OK".equals(response.get("status"))) {
            throw new EshopException("Location not found");
        }

        List<Map<String, Object>> predictions = (List<Map<String, Object>>) response.get("predictions");

        // Extract distinct `main_text` values
        return predictions.stream()
                .map(p -> (String) ((Map<String, Object>) p.get("structured_formatting")).get("main_text")).distinct()
                .collect(Collectors.toList());
    }
}
