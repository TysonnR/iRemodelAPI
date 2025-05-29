package com.iremodelapi.service;

import com.iremodelapi.domain.Contractor;
import com.iremodelapi.domain.Job;
import com.iremodelapi.repository.ContractorRepository;
import com.iremodelapi.repository.HomeownerRepository;
import com.iremodelapi.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for Job-related business logic and operations.
 * Handles job creation, contractor assignment, status management, and job queries.
 * Acts as the business logic layer between controllers and repositories.
 *
 * SPRING SERVICE LAYER PATTERN:
 * This follows the classic enterprise architecture pattern where:
 * - Controllers handle HTTP requests/responses
 * - Services contain business logic and validation
 * - Repositories handle database operations
 *
 * This layer is more accessible than security configuration but still demonstrates
 * enterprise Spring patterns like dependency injection, transaction management,
 * and service layer architecture.
 *
 * @author Tyson Ringelstetter
 * @date 5/28/2025
 */
/*
    SPRING SERVICE LAYER FEATURES:

    @Service: Marks this as a Spring service component for dependency injection
    - Automatically detected by Spring's component scanning
    - Registered as a bean in Spring's application context
    - Available for injection into controllers and other components

    Constructor Dependency Injection: Modern Spring pattern
    - Dependencies injected through constructor (preferred over field injection)
    - final fields ensure dependencies are immutable after construction
    - Enables easier testing and ensures required dependencies are available

    Business Logic Layer: Separates business rules from web/database concerns
    - Input validation and business rule enforcement
    - Coordination between multiple repositories
    - Transaction management (handled automatically by Spring)

*/
@Service
public class JobService
{
    /*
        Repository Dependencies - SPRING DEPENDENCY INJECTION:

        private final: Makes dependencies immutable after construction
        Multiple repositories because the service layer coordinates between multiple data sources
        Using Constructor Injection follows modern best practices for dependency injection
    */

    private final JobRepository jobRepository;
    private final ContractorRepository contractorRepository;
    private final HomeownerRepository homeownerRepository;

    /**
     * Constructor for dependency injection of required repositories.
     * Spring automatically provides these dependencies based on @Repository beans.
     *
     * @param jobRepository Repository for job database operations
     * @param contractorRepository Repository for contractor database operations
     * @param homeownerRepository Repository for homeowner database operations
     */
    public JobService(JobRepository jobRepository, ContractorRepository contractorRepository, HomeownerRepository homeownerRepository)
    {
        this.jobRepository = jobRepository;
        this.contractorRepository = contractorRepository;
        this.homeownerRepository = homeownerRepository;
    }

    /**
     * Finds a job by its unique ID.
     * Throws RuntimeException if job doesn't exist.
     *
     * @param jobId The unique identifier of the job to find
     * @return Job entity with the specified ID
     * @throws RuntimeException if no job found with the given ID
     */
    public Job findJobById(Long jobId)
    {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
    }

    /**
     * Creates a new job with business rule validation.
     * Validates required fields and business constraints before saving to database.
     *
     * BUSINESS RULES ENFORCED:
     * - Budget must be greater than zero
     * - Job title cannot be null or empty
     * - Homeowner must be specified
     * - Status defaults to OPEN if not provided
     *
     * @param job The job entity to create (with validation)
     * @return The saved job entity with generated ID and timestamps
     * @throws IllegalArgumentException if business rule validation fails
     */
    public Job createJob(Job job)
    {
        // Validate budget is positive (BigDecimal comparison for financial precision)
        if (job.getBudget() == null || job.getBudget().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Budget must be greater than zero");
        }
        // Make sure job title was provided
        if (job.getTitle() == null || job.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Job title is required");
        }
        // Validate the homeowner was provided
        if (job.getHomeowner() == null)
        {
            throw new IllegalArgumentException("Homeowner is required");
        }
        // Set to default status if not provided
        if (job.getStatus() == null)
        {
            job.setStatus(Job.JobStatus.OPEN);
        }
        // Save the new valdated job to the database
        return jobRepository.save(job);
    }

    /**
     * Assigns a contractor to an open job and updates both entities.
     * Handles the bidirectional relationship between Job and Contractor entities.
     *
     * RELATIONSHIP MANAGEMENT:
     * - Updates job's assigned contractor
     * - Updates contractor's job list
     * - Changes job status to IN_PROGRESS
     *
     * @param jobId The ID of the job to assign contractor to
     * @param contractorId The ID of the contractor to assign to the job
     * @return The updated job entity with contractor assignment
     * @throws RuntimeException if job or contractor not found
     */
    public Job assignContractorToJob(Long jobId, Long contractorId)
    {
        // Load both entities (will throw exception if not found)
        Job job = findJobById(jobId);
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found with id: " + contractorId));

        // Use business logic method with validation method to assign contractor to a job
        job.assignContractor(contractor);
        // Maintains the many-to-one relationship
        contractor.addJob(job);
        // Save the update job
        return jobRepository.save(job);

    }

    /**
     * Retrieves all jobs in the system.
     * uses the findAll method in the database layer
     *
     * @return List of all job entities
     */
    public List<Job> getAllJobs()
    {
        return jobRepository.findAll();
    }

    /**
     * Finds jobs by location zip code
     * uses the findJobsByZipCode in the database layer.
     *
     * @param zipCode The zip code to search for jobs in
     * @return List of jobs in the specified zip code area
     */
    public List<Job> findJobsByZipCode(String zipCode)
    {
        return jobRepository.findByZipCode(zipCode);
    }

    /**
     * Finds all jobs posted by a specific homeowner.
     *
     * @param homeownerId The ID of the homeowner to find jobs for
     * @return List of jobs posted by the specified homeowner
     */
    public List<Job> findJobsByHomeownerId(Long homeownerId)
    {
        return jobRepository.findByHomeownerId(homeownerId);
    }

    /**
     * Finds all jobs assigned to a specific contractor.
     * Meant to help contractors track their current workload and job history.
     *
     * @param contractorId The ID of the contractor to find jobs for
     * @return List of jobs assigned to the specified contractor
     */
    public List<Job> findJobsByContractor(Long contractorId)
    {
        return jobRepository.findByAssignedContractorId(contractorId);
    }

    /**
     * Marks a job as completed and updates its status.
     * Uses the Job entity's business logic method for status transition validation.
     *
     * BUSINESS LOGIC DELEGATION:
     * - job.markCompleted() handles status validation and transition
     * - Only IN_PROGRESS jobs can be marked as COMPLETED
     * - Business rules enforced at entity level for consistency
     *
     * @param jobId The ID of the job to mark as completed
     * @return The updated job entity with COMPLETED status
     * @throws RuntimeException if job not found
     * @throws IllegalStateException if job cannot be completed (from the entity logic)
     */
    public Job completeJob(Long jobId)
    {
        Job job = findJobById(jobId);

        job.markCompleted();

        return jobRepository.save(job);
    }
}
