package com.iremodelapi.web.controller;

import com.iremodelapi.service.MatchService;
import com.iremodelapi.web.dto.ContractorMatchDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for contractor matching operations.
 * Exposes the contractor-to-job matching algorithm via RESTful API endpoints.
 * Provides access to the QuickSort-powered matching service for finding suitable contractors.
 *
 * ALGORITHM INTEGRATION:
 * This controller serves as the web layer entry point to the sophisticated contractor
 * matching algorithm implemented in MatchService, which combines:
 * - Custom business scoring logic (specialty, location, rating weights)
 * - Computer Science algorithms (QuickSort for ranking results)
 * - Professional API response formatting (DTOs with match explanations)
 *
 * LEARNING ACKNOWLEDGMENT:
 * The REST API patterns and ResponseEntity usage involved learning from Spring MVC
 * documentation and examples, though this represents a more focused controller
 * compared to complex authentication or broad CRUD operations.
 *
 * @author Tyson Ringelstetter
 * @date 05/29/2025
 */
/*
    SPECIALIZED CONTROLLER PATTERN:

    @RestController: Automatic JSON serialization for API responses
    @RequestMapping("/api/jobs"): Groups matching endpoints under job resource

    This controller demonstrates a specialized business operation controller:
    - Single-purpose: Focuses solely on contractor matching functionality
    - Algorithm exposure: Makes complex business logic available via REST API
    - DTO usage: Returns structured API-friendly objects instead of domain entities
    - Error handling: Proper HTTP status codes for different scenarios

*/
@RestController
@RequestMapping("/api/jobs")
public class MatchController
{
    private final MatchService matchService;

    /**
     * Constructor for dependency injection of matching service.
     *
     * @param matchService Service containing contractor matching algorithm and business logic
     */
    public MatchController(MatchService matchService)
    {
        this.matchService = matchService;
    }

    /**
     * Finds and ranks contractors suitable for a specific job.
     * Exposes the sophisticated contractor matching algorithm via REST API.
     *
     * HTTP ENDPOINT: GET /api/jobs/{jobId}/matches
     * PATH VARIABLE: jobId - unique identifier of the job to find contractors for
     * RESPONSE: JSON array of ranked contractor matches with scores and explanations
     *
     * @param jobId The unique identifier of the job to find contractor matches for
     * @return ResponseEntity containing list of contractor matches or error response
     */
    @GetMapping("/{jobId}/matches")
    @Transactional
    public ResponseEntity<List<ContractorMatchDTO>> getMatchesForJob(@PathVariable Long jobId)
    {
        try
        {
            List<ContractorMatchDTO> matches = matchService.findMatches(jobId);
            return ResponseEntity.ok(matches);
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.notFound().build(); //404 error
        }

    }

}
