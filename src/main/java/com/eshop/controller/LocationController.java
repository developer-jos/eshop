package com.eshop.controller;

import com.eshop.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for handling location-based suggestions.
 */
@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService service;

    /**
     * Provides place suggestions based on the input and location.
     *
     * @param input the search input
     * @param latitude the latitude of the location
     * @param longitude the longitude of the location
     * @param radius the radius to search within (default: 500000)
     * @return a list of place suggestions
     */
    @GetMapping("/api/v1/places/suggestions")
    public ResponseEntity<List<String>> getPlaceSuggestions(@RequestParam String input, @RequestParam double latitude,
                                                            @RequestParam double longitude,
                                                            @RequestParam(defaultValue = "500000") int radius) {

        List<String> suggestions = service.getPlaceSuggestions(input, latitude, longitude, radius);
        return ResponseEntity.ok(suggestions);
    }
}
