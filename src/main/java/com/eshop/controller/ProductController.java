package com.eshop.controller;

import com.eshop.dto.PaginatedResponseDTO;
import com.eshop.dto.ProductResponseDTO;
import com.eshop.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling product-related requests.
 */
@RestController
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    /**
     * Retrieves paginated products by location name.
     *
     * @param locationName the name of the location
     * @param pageable the pagination information
     * @return a paginated list of products for the specified location
     */
    @GetMapping("/api/v1/products/{location-name}")
    public ResponseEntity<PaginatedResponseDTO<ProductResponseDTO>> getProductsByLocationName(
            @PathVariable("location-name") String locationName, Pageable pageable) {

        return ResponseEntity.ok(productService.getProductsByLocationName(locationName, pageable));
    }
}
