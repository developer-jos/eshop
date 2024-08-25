package com.eshop.repository;

import com.eshop.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing {@link Location} entities.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    /**
     * Finds a location by its name, ignoring case.
     *
     * @param locationName the name of the location
     * @return an {@link Optional} containing the found {@link Location}, or empty if not found
     */
    @Query("SELECT l FROM Location l WHERE LOWER(l.locationName) = LOWER(:locationName)")
    Optional<Location> findByLocationName(String locationName);
}
