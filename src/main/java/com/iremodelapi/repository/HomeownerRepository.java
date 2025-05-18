package com.iremodelapi.repository;

import com.iremodelapi.domain.Homeowner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Homeowner entity operations.
 */
@Repository
public interface HomeownerRepository extends JpaRepository<Homeowner, Long> {

    /**
     * Find homeowners who have properties in a specific area.
     *
     * @param zipCode The zip code to search for
     * @return List of homeowners with properties in the area
     */
    @Query("SELECT h FROM Homeowner h JOIN h.propertyAddresses pa WHERE pa LIKE CONCAT('%', :zipCode, '%')")
    List<Homeowner> findByPropertyAddressContaining(String zipCode);

    /**
     * Find homeowners by preferred contact method.
     *
     * @param contactMethod The preferred contact method
     * @return List of homeowners with the given contact preference
     */
    List<Homeowner> findByPreferredContactMethod(String contactMethod);
}