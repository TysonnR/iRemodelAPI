package com.iremodelapi.repository;

import com.iremodelapi.domain.Contractor;
import com.iremodelapi.domain.Contractor.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Contractor entity operations.
 */
@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Long> {

    /**
     * Find contractors by service area zip code.
     *
     * @param zipCode The zip code to search for
     * @return List of contractors serving the area
     */
    @Query("SELECT c FROM Contractor c JOIN c.serviceAreas sa WHERE sa = :zipCode")
    List<Contractor> findByServiceArea(String zipCode);

    /**
     * Find contractors by specialty.
     *
     * @param specialty The specialty to search for
     * @return List of contractors with the given specialty
     */
    @Query("SELECT c FROM Contractor c JOIN c.specialties s WHERE s = :specialty")
    List<Contractor> findBySpecialty(Specialty specialty);

    /**
     * Find contractors by rating above a threshold.
     *
     * @param minRating The minimum rating to filter by
     * @return List of contractors with rating >= minRating
     */
    List<Contractor> findByRatingGreaterThanEqual(Double minRating);
}