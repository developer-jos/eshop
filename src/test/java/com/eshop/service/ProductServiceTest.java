package com.eshop.service;

import com.eshop.dto.PaginatedResponseDTO;
import com.eshop.dto.ProductResponseDTO;
import com.eshop.entity.Location;
import com.eshop.entity.Product;
import com.eshop.exception.EshopException;
import com.eshop.repository.LocationRepository;
import com.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Pageable pageable;

    @BeforeEach
    void setUp() {

        pageable = PageRequest.of(0, 10); // First page, 10 items per page
    }

    @Test
    void testGetProductsByLocationName_Success() {
        // Arrange
        String locationName = "Location A";
        Location location = new Location(); // Create a mock location
        location.setLocationId(1);
        location.setLocationName(locationName);

        Product product = new Product(); // Create a mock product
        product.setProductId(1L);
        product.setProductName("Product 1");
        Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);

        when(locationRepository.findByLocationName(locationName)).thenReturn(Optional.of(location));
        when(productRepository.findByLocation(location, pageable)).thenReturn(Optional.of(productPage));

        // Act
        PaginatedResponseDTO<ProductResponseDTO> response =
                productService.getProductsByLocationName(locationName, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals("Product 1", response.getItems().get(0).getProductName());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());

        verify(locationRepository, times(1)).findByLocationName(locationName);
        verify(productRepository, times(1)).findByLocation(location, pageable);
    }

    @Test
    void testGetProductsByLocationName_LocationNotFound() {
        // Arrange
        String locationName = "Non-Existent Location";
        when(locationRepository.findByLocationName(locationName)).thenReturn(Optional.empty());

        // Act & Assert
        EshopException exception = assertThrows(EshopException.class,
                                                () -> productService.getProductsByLocationName(locationName, pageable));
        assertEquals("Location not found: "+locationName, exception.getMessage());

        verify(locationRepository, times(1)).findByLocationName(locationName);
        verify(productRepository, never()).findByLocation(any(Location.class), eq(pageable));
    }

    @Test
    void testGetProductsByLocationName_NoProductsFound() {
        // Arrange
        String locationName = "Location A";
        Location location = new Location(); // Create a mock location
        location.setLocationId(1);
        location.setLocationName(locationName);

        when(locationRepository.findByLocationName(locationName)).thenReturn(Optional.of(location));
        when(productRepository.findByLocation(location, pageable)).thenReturn(Optional.empty());

        // Act & Assert
        EshopException exception = assertThrows(EshopException.class,
                                                () -> productService.getProductsByLocationName(locationName, pageable));
        assertEquals("No products found for location: "+locationName, exception.getMessage());

        verify(locationRepository, times(1)).findByLocationName(locationName);
        verify(productRepository, times(1)).findByLocation(location, pageable);
    }

    @Test
    void testGetProductsByLocationName_EmptyProducts() {
        // Arrange
        String locationName = "Location A";
        Location location = new Location(); // Create a mock location
        location.setLocationId(1);
        location.setLocationName(locationName);
        Page<Product> productPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(locationRepository.findByLocationName(locationName)).thenReturn(Optional.of(location));
        when(productRepository.findByLocation(location, pageable)).thenReturn(Optional.of(productPage));

        // Act
        PaginatedResponseDTO<ProductResponseDTO> response =
                productService.getProductsByLocationName(locationName, pageable);

        // Assert
        assertNotNull(response);
        assertTrue(response.getItems().isEmpty());
        assertEquals(0, response.getTotalElements());
        assertEquals(0, response.getTotalPages());

        verify(locationRepository, times(1)).findByLocationName(locationName);
        verify(productRepository, times(1)).findByLocation(location, pageable);
    }
}