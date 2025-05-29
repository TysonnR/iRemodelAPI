package com.iremodelapi.repository;

import com.iremodelapi.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Job entity database operations.
 * Extends Spring Data JPA's JpaRepository to provide automatic CRUD operations
 * plus custom finder methods using Spring Data JPA method naming conventions.
 *
 * SPRING FRAMEWORK MAGIC: Spring automatically generates the implementation class
 * at runtime - you never write the actual implementation code!
 *
 * This repository demonstrates pure method naming conventions - no custom @Query annotations needed!
 *
 * @author Tyson Ringelstetter
 * @date 5/28/2025
 */
/*
    SPRING DATA JPA ENTERPRISE FEATURES (Beyond Basic Java):

    @Repository: Marks this interface as a Spring repository component for dependency injection
    and exception translation (converts database exceptions to Spring exceptions).

    JpaRepository<Job, Long>:
    - Job: The entity type this repository manages
    - Long: The type of the entity's primary key (ID field)
    - Automatically provides: save(), findById(), findAll(), delete(), count(), etc.

    This is VERY different from basic Java interfaces - Spring creates the implementation
    class automatically using reflection and proxies. You never write the actual code!

*/

@Repository
public interface JobRepository extends JpaRepository<Job, Long>
{
    /*
        Spring Data JPA Method Naming Convention - AUTOMATIC QUERY GENERATION:

        All methods below use Spring's method naming conventions to automatically generate queries.
        Spring parses the method names and creates the appropriate JPQL/SQL queries at runtime.

        Pattern: findBy[FieldName][Operator](parameters)
        - "findBy" - SELECT operation
        - "FieldName" - Use the specified entity field
        - "Operator" - Optional (defaults to equals)
        - Parameters become WHERE clause conditions

        This is "convention over configuration" - following naming patterns eliminates
        the need for manual query writing!

        NOTE: I used AI assistance to help determine the correct method naming patterns
        since Spring Data JPA conventions can be complex with relationship fields and enums.
    */
    /**
     * Finds jobs by location zip code.
     * Spring converts method name to: WHERE zipCode = :zipCode
     *
     * @param zipCode The zip code where the job is located
     * @return List of jobs in the specified zip code area
     */
    List<Job> findByZipCode(String zipCode);

    /**
     * Finds jobs by their current status.
     * Spring converts method name to: WHERE status = :status
     * Works with enum values automatically
     *
     * @param status The job status to filter by (OPEN, IN_PROGRESS, COMPLETED, CANCELLED)
     * @return List of jobs with the specified status
     */
    List<Job> findByStatus(Job.JobStatus status);

    /**
     * Finds jobs by category/type of work.
     * Spring converts method name to: WHERE category = :category
     * Works with enum values automatically!
     *
     * @param category The job category to filter by (PLUMBING, ELECTRICAL, etc.)
     * @return List of jobs in the specified category
     */
    List<Job> findByCategory(Job.JobCategory category);

    /**
     * Finds all jobs posted by a specific homeowner.
     * Spring converts method name to: WHERE homeowner.id = :homeownerId
     * Automatically handles the relationship navigation!
     *
     * @param homeownerId The ID of the homeowner who posted the jobs
     * @return List of jobs posted by the specified homeowner
     */
    List<Job> findByHomeownerId(Long homeownerId);

    /**
     * Finds all jobs assigned to a specific contractor.
     * Spring converts method name to: WHERE assignedContractor.id = :contractorId
     * Automatically handles the relationship navigation!
     *
     * @param contractorId The ID of the contractor assigned to the jobs
     * @return List of jobs assigned to the specified contractor
     */
    List<Job> findByAssignedContractorId(Long contractorId);


}

