package com.eshop.controller;

import com.eshop.dto.PaginatedResponseDTO;
import com.eshop.dto.ProductResponseDTO;
import com.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Pageable pageable;

    @BeforeEach
    void setUp() {

        pageable = PageRequest.of(0, 10); // First page, 10 items per page
    }

    @Test
    void testGetProductsByLocationName_Success() {
        // Arrange
        String locationName = "Location A";
        ProductResponseDTO productResponseDTO = new ProductResponseDTO(1L, "Product 1");
        List<ProductResponseDTO> productList = List.of(productResponseDTO);
        PaginatedResponseDTO<ProductResponseDTO> paginatedResponseDTO =
                new PaginatedResponseDTO<>(productList, 1L, 1, 0, 10);

        when(productService.getProductsByLocationName(locationName, pageable)).thenReturn(paginatedResponseDTO);

        // Act
        ResponseEntity<PaginatedResponseDTO<ProductResponseDTO>> responseEntity =
                productController.getProductsByLocationName(locationName, pageable);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(paginatedResponseDTO, responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().getTotalElements());
        assertEquals(1, responseEntity.getBody().getTotalPages());
        assertEquals("Product 1", responseEntity.getBody().getItems().get(0).getProductName());

        verify(productService, times(1)).getProductsByLocationName(locationName, pageable);
    }

    @Test
    void testGetProductsByLocationName_NoProductsFound() {
        // Arrange
        String locationName = "Location B";
        PaginatedResponseDTO<ProductResponseDTO> emptyResponseDTO = new PaginatedResponseDTO<>(List.of(), 0L, 0, 0, 10);

        when(productService.getProductsByLocationName(locationName, pageable)).thenReturn(emptyResponseDTO);

        // Act
        ResponseEntity<PaginatedResponseDTO<ProductResponseDTO>> responseEntity =
                productController.getProductsByLocationName(locationName, pageable);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(emptyResponseDTO, responseEntity.getBody());
        assertEquals(0, responseEntity.getBody().getTotalElements());
        assertEquals(0, responseEntity.getBody().getTotalPages());

        verify(productService, times(1)).getProductsByLocationName(locationName, pageable);
    }

    @Test
    void testGetProductsByLocationName_ServiceThrowsException() {
        // Arrange
        String locationName = "Invalid Location";
        when(productService.getProductsByLocationName(locationName, pageable)).thenThrow(
                new RuntimeException("Service error"));

        // Act & Assert
        try {
            productController.getProductsByLocationName(locationName, pageable);
        } catch(RuntimeException e) {
            assertEquals("Service error", e.getMessage());
        }

        verify(productService, times(1)).getProductsByLocationName(locationName, pageable);
    }
}
