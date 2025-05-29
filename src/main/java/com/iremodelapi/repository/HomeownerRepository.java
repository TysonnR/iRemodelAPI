package com.iremodelapi.repository;

import com.iremodelapi.domain.Homeowner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Homeowner entity database operations.
 * Extends Spring Data JPA's JpaRepository to provide automatic CRUD operations
 * plus custom query methods for homeowner-specific business logic.
 *
 * SPRING FRAMEWORK MAGIC: Spring automatically generates the implementation class
 * at runtime - you never write the actual implementation code!
 *
 * @author Tyson Ringelstetter
 * @date 05/28/2025
 */
/*
    SPRING DATA JPA FEATURES (Beyond Basic Java):

    @Repository: Marks this interface as a Spring repository component for dependency injection
    and exception translation (converts database exceptions to Spring exceptions).

    JpaRepository<Homeowner, Long>:
    - Homeowner: The entity type this repository manages
    - Long: The type of the entity's primary key (ID field)
    - Automatically provides: save(), findById(), findAll(), delete(), count(), etc.

    This is VERY different from basic Java interfaces - Spring creates the implementation
    class automatically using reflection and proxies. You never write the actual code!

*/
@Repository
public interface HomeownerRepository extends JpaRepository<Homeowner, Long>
{

    /*
        Custom Query Methods - SPRING DATA JPA + JPQL FEATURES:

        @Query annotation allows writing of custom JPQL (Java Persistence Query Language)
        queries. JPQL looks like SQL but works with Java objects instead of database tables.

        "SELECT h FROM Homeowner h" - Selects Homeowner entities (not table rows!)
        "JOIN h.propertyAddresses pa" - Joins the propertyAddresses collection from Homeowner entity
        "LIKE CONCAT('%', :zipCode, '%')" - Pattern matching to find zip codes within addresses
        ":zipCode" - Parameter placeholder that gets replaced with method parameter

        NOTE: I used AI assistance to help write the JPQL queries since this is advanced
        database query syntax beyond basic Java & my current knowledge

    */

    /**
     * Finds homeowners who have properties containing a specific zip code.
     * Uses JPQL to join with the homeowner's propertyAddresses collection and
     * performs pattern matching to find zip codes within full addresses.
     *
     * @param zipCode The zip code to search for within property addresses
     * @return List of homeowners who have properties in areas containing the zip code
     */
    @Query("SELECT h FROM Homeowner h JOIN h.propertyAddresses pa WHERE pa LIKE CONCAT('%', :zipCode, '%')")
    List<Homeowner> findByPropertyAddressContaining(String zipCode);

    /**
     * Finds homeowners by their preferred contact method.
     * Uses Spring Data JPA method naming convention to automatically generate the query.
     * Spring converts method name to: WHERE preferredContactMethod = contactMethod
     *
     * @param contactMethod The preferred contact method to filter by ("email", "phone", "text")
     * @return List of homeowners with the specified preferred contact method
     */
    List<Homeowner> findByPreferredContactMethod(String contactMethod);
}