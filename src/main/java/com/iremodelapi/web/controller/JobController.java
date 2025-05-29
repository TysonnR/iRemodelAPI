package com.iremodelapi.web.controller;

import com.iremodelapi.domain.Job;
import com.iremodelapi.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for job-related operations in the iRemodel API.
 * Provides CRUD operations and business-specific endpoints for job management.
 * Follows RESTful API design principles with proper HTTP method usage.
 *
 * LEARNING ACKNOWLEDGMENT:
 * This controller implementation involved learning Spring MVC patterns and
 * RESTful API design principles from documentation and examples, though it
 * represents more straightforward web controller patterns compared to the
 * authentication controller's complex security
 *
 * This follows standard entity controller patterns commonly
 * used in enterprise web applications.
 *
 * @author Tyson Ringelstetter
 * @date 05/28/2025
 */
@RestController
@RequestMapping("/api/jobs")
public class JobController
{
    /*
        Single Service Dependency - CLEAN ARCHITECTURE.
        Controller focuses on web concerns, delegates business logic to service
        Simple dependency injection pattern for business operations
    */
    private final JobService jobService;

    /**
     * Constructor for dependency injection of job service.
     *
     * @param jobService Service containing job business logic operations
     */
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    /**
     * Retrieves all jobs in the system.
     *
     * HTTP ENDPOINT: GET /api/jobs
     * RESPONSE: JSON array of all job objects
     * @return List of all job entities
     */
    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    /**
     * Retrieves a specific job by its unique identifier.
     *
     * HTTP ENDPOINT: GET /api/jobs/{jobId}
     * PATH VARIABLE: jobId - unique identifier of the job
     * RESPONSE: JSON object of the requested job
     *
     * @param jobId The unique identifier of the job to retrieve
     * @return Job entity with the specified ID
     */
    @GetMapping("/{jobId}")
    public Job getJobById(@PathVariable Long jobId) {
        return jobService.findJobById(jobId);
    }

    /**
     * Searches for jobs by location zip code.
     *
     * HTTP ENDPOINT: GET /api/jobs/search?zipCode=12345
     * QUERY PARAMETER: zipCode - location to search for jobs
     * RESPONSE: JSON array of jobs in the specified location
     *
     * @param zipCode The zip code to search for jobs in
     * @return List of jobs located in the specified zip code area
     */
    @GetMapping("/search")
    public List<Job> searchJobsByZipCode(@RequestParam String zipCode) {
        return jobService.findJobsByZipCode(zipCode);
    }

    /**
     * Creates a new job in the system.
     *
     * HTTP ENDPOINT: POST /api/jobs
     * REQUEST BODY: JSON object containing job details
     * RESPONSE: JSON object of the created job with generated ID
     *
     * @param job Job entity containing the details for the new job
     * @return Created job entity with generated ID and default values
     */
    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    /**
     * Marks a job as completed.
     *
     * HTTP ENDPOINT: PUT /api/jobs/{jobId}/complete
     * PATH VARIABLE: jobId - identifier of job to complete
     * RESPONSE: JSON object of the updated job with COMPLETED status
     *
     * @param jobId The unique identifier of the job to mark as completed
     * @return Updated job entity with COMPLETED status
     */
    @PutMapping("/{jobId}/complete")
    public Job completeJob(@PathVariable Long jobId) {
        return jobService.completeJob(jobId);
    }

    /**
     * Assigns a contractor to a job.
     *
     * HTTP ENDPOINT: PUT /api/jobs/{jobId}/assign/{contractorId}
     * PATH VARIABLES:
     *   - jobId: identifier of job to assign contractor to
     *   - contractorId: identifier of the contractor to assign
     * RESPONSE: JSON object of updated job with assigned contractor

     * @param jobId The unique identifier of the job
     * @param contractorId The unique identifier of the contractor to assign
     * @return Updated job entity with assigned contractor and IN_PROGRESS status
     */
    @PutMapping("/{jobId}/assign/{contractorId}")
    public Job assignContractorToJob(@PathVariable Long jobId, @PathVariable Long contractorId)
    {
        return jobService.assignContractorToJob(jobId, contractorId);
    }

}
