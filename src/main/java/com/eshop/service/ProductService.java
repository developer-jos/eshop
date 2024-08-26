package com.eshop.service;

import com.eshop.dto.PaginatedResponseDTO;
import com.eshop.dto.ProductResponseDTO;
import com.eshop.entity.Location;
import com.eshop.entity.Product;
import com.eshop.exception.EshopException;
import com.eshop.repository.LocationRepository;
import com.eshop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for handling product-related operations.
 */
@Service
@AllArgsConstructor
public class ProductService {

    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;

    /**
     * Retrieves paginated products by location name.
     *
     * @param locationName the name of the location
     * @param pageable the pagination information
     * @return a paginated response containing a list of product DTOs
     * @exception EshopException if the location is not found or no products are found for the location
     */
    @Transactional
    public PaginatedResponseDTO<ProductResponseDTO> getProductsByLocationName(String locationName, Pageable pageable) {
        // Fetch the location by name
        Location location = locationRepository.findByLocationName(locationName)
                .orElseThrow(() -> new EshopException("Location not found: "+locationName));

        // Fetch products with pagination and handle the optional result
        Page<Product> productPage = productRepository.findByLocation(location, pageable)
                .orElseThrow(() -> new EshopException("No products found for location: "+locationName));

        // Convert Product entities to ProductResponseDTO
        List<ProductResponseDTO> productResponseDTOs = productPage.stream()
                .map(product -> new ProductResponseDTO(product.getProductId(), product.getProductName()))
                .collect(Collectors.toList());

        // Return the paginated response
        return new PaginatedResponseDTO<>(productResponseDTOs, productPage.getTotalElements(),
                                          productPage.getTotalPages(), productPage.getNumber(), productPage.getSize());
    }
}
