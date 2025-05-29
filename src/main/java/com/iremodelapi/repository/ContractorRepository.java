package com.iremodelapi.repository;

import com.iremodelapi.domain.Contractor;
import com.iremodelapi.domain.Contractor.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Contractor entity database operations.
 * Extends Spring Data JPA's JpaRepository to provide automatic CRUD operations
 * plus custom query methods for contractor-specific business logic.
 *
 * SPRING FRAMEWORK MAGIC: Spring automatically generates the implementation class
 * at runtime - you never write the actual implementation code!
 *
 * @author Tyson Ringelstetter
 * @date 5/28/2025
 */
/*
    SPRING DATA JPA FEATURES (Beyond Basic Java):

    @Repository: Marks this interface as a Spring repository component for dependency injection
    and exception translation (converts database exceptions to Spring exceptions).

    JpaRepository<Contractor, Long>:
    - Contractor: The entity type this repository manages
    - Long: The type of the entity's primary key (ID field)
    - Automatically provides: save(), findById(), findAll(), delete(), count(), etc.

    This is VERY different from basic Java interfaces - Spring creates the implementation
    class automatically using reflection and proxies. Spring manages all of this behind the scenes

    NOTE: Without Spring Data JPA, you'd need to write hundreds of lines of SQL and
    JDBC code to perform these database operations manually.


*/
@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Long>
{

    /*
        Custom Query Methods - SPRING DATA JPA + JPQL FEATURES:

        @Query annotation allows writing of custom JPQL (Java Persistence Query Language)
        queries. JPQL looks like SQL but works with Java objects instead of database tables.

        "SELECT c FROM Contractor c" - Selects Contractor entities
        "JOIN c.serviceAreas sa" - Joins the serviceAreas collection from Contractor entity
        ":zipCode" - Parameter placeholder that gets replaced with method parameter

        This is advanced & combines JPA entity relationships with custom queries

        NOTE: I used AI assistance to help write the JPQL queries since this is advanced
        database query syntax beyond basic Java and SQL
    */

    /**
     * Finds contractors who service a specific zip code area.
     * Uses JPQL to join with the contractor's serviceAreas collection.
     *
     * @param zipCode The zip code to search for in contractor service areas
     * @return List of contractors who service the given zip code area
     */
    @Query("SELECT c FROM Contractor c JOIN c.serviceAreas sa WHERE sa = :zipCode")
    List<Contractor> findByServiceArea(String zipCode);

    /**
     * Finds contractors who have a specific specialty.
     * Uses JPQL to join with the contractor's specialties collection.
     *
     * @param specialty The specialty to search for in contractor specialties
     * @return List of contractors who have the given specialty
     */
    @Query("SELECT c FROM Contractor c JOIN c.specialties s WHERE s = :specialty")
    List<Contractor> findBySpecialty(Specialty specialty);

    /*
        Spring Data JPA Method Naming Convention AUTOMATIC QUERY GENERATION:

        Spring Data JPA can automatically generate queries based on method names!
        "findByRatingGreaterThanEqual" gets parsed as:
        - "findBy" - SELECT operation
        - "Rating" - Use the 'rating' field
        - "GreaterThanEqual" - WHERE rating >= parameter

        Spring automatically generates: "SELECT c FROM Contractor c WHERE c.rating >= :minRating"

        No @Query annotation needed, Spring figures it out from the method name!
    */

    /**
     * Finds contractors with rating above or equal to a minimum threshold.
     * Uses Spring Data JPA method naming convention to automatically generate the query.
     * Spring converts method name to: WHERE rating >= minRating
     *
     * @param minRating The minimum rating threshold to filter contractors
     * @return List of contractors with rating greater than or equal to minRating
     */
    List<Contractor> findByRatingGreaterThanEqual(Double minRating);
}