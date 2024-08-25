package com.eshop.repository;

import com.eshop.entity.Location;
import com.eshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing {@link Product} entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds products by location with pagination.
     *
     * @param location the location entity to search by
     * @param pageable the pagination information
     * @return an {@link Optional} containing a page of {@link Product} entities, or empty if no products are found
     */
    Optional<Page<Product>> findByLocation(Location location, Pageable pageable);
}
