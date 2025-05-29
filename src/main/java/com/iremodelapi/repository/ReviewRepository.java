package com.iremodelapi.repository;

import com.iremodelapi.domain.Review;
import com.iremodelapi.domain.Contractor;
import com.iremodelapi.domain.Homeowner;
import com.iremodelapi.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Review entity database operations.
 * Extends Spring Data JPA's JpaRepository to provide automatic CRUD operations
 * plus custom finder methods and aggregate functions for review analytics.
 *
 * SPRING FRAMEWORK MAGIC: Spring automatically generates the implementation class
 * at runtime - you never write the actual implementation code!
 *
 * This repository demonstrates method naming with entity parameters and aggregate queries.
 *
 * @author Tyson Ringelstetter
 * @date 05/28/2025
 */
/*
    SPRING DATA JPA FEATURES (Beyond Basic Java):

    @Repository: Marks this interface as a Spring repository component for dependency injection
    and exception translation (converts database exceptions to Spring exceptions).

    JpaRepository<Review, Long>:
    - Review: The entity type this repository manages
    - Long: The type of the entity's primary key (ID field)
    - Automatically provides: save(), findById(), findAll(), delete(), count(), etc.

    This is VERY different from basic Java interfaces, Spring creates the implementation
    class automatically using reflection and proxies. You never write the actual code!
    This helps alot as it would be hundreds of lines of SQL & JDBC code.

*/

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>
{
    /*
       Spring Data JPA Method Naming with Entity Parameters - AUTOMATIC QUERY GENERATION:

       These methods use entire entity objects as parameters, not just IDs!
       Spring Data JPA automatically handles the entity-to-query conversion:

       "findByContractor(Contractor contractor)" becomes:
       "SELECT r FROM Review r WHERE r.contractor = :contractor"

       Spring automatically uses the entity's primary key for comparison in the WHERE clause.
       This is more convenient than using IDs and allows for type safety.

       This is "convention over configuration" - following naming patterns with entity types
       reduces code and provides better type safety than using primitive IDs.
   */
    /**
     * Finds all reviews written about a specific contractor.
     * Spring converts method name to: WHERE contractor = :contractor
     * Uses the contractor entity's primary key automatically for comparison.
     *
     * @param contractor The contractor entity to find reviews for
     * @return List of reviews about the specified contractor
     */
    List<Review> findByContractor(Contractor contractor);

    /**
     * Finds all reviews written by a specific homeowner.
     * Spring converts method name to: WHERE homeowner = :homeowner
     * Uses the homeowner entity's primary key automatically for comparison.
     *
     * @param homeowner The homeowner entity to find reviews by
     * @return List of reviews written by the specified homeowner
     */
    List<Review> findByHomeowner(Homeowner homeowner);

    /**
     * Finds all reviews for a specific job.
     * Spring converts method name to: WHERE job = :job
     * Uses the job entity's primary key automatically for comparison.
     *
     * @param job The job entity to find reviews for
     * @return List of reviews for the specified job
     */
    List<Review> findByJob(Job job);

    /**
     * Calculates the average rating for a specific contractor across all their reviews.
     * Uses JPQL aggregate function AVG() to compute the average rating value.
     *
     * Returns null if the contractor has no reviews (important for null safety!).
     * This method supports the Contractor.updateRatingFromReviews() business logic.
     *
     * @param contractor The contractor entity to calculate average rating for
     * @return Average rating as Double, or null if no reviews exist
     */
    /*
        NOTE: I used AI assistance to help write this  JPQL query since
        database functions and null handling are advanced concepts beyond
        basic Java
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.contractor = :contractor")
    Double calculateAverageRatingForContractor(Contractor contractor);
}
