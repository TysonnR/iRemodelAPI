package com.iremodelapi.service;

import com.iremodelapi.domain.Contractor;
import com.iremodelapi.domain.Job;
import com.iremodelapi.repository.ContractorRepository;
import com.iremodelapi.repository.JobRepository;
import com.iremodelapi.web.dto.ContractorMatchDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * Service class for contractor-to-job matching algorithm implementation.
 * Implements the core business logic for finding and ranking contractors suitable for specific jobs.

 * BUSINESS LOGIC:
 * Uses weighted scoring system based on iRemodel platform requirements:
 * - Specialty Match: 50% weight
 * - Location Proximity: 30% weight
 * - Contractor Rating: 20% weight
 * - Minimum Score Threshold: 35 points to qualify as a match

 * @author Tyson Ringelstetter
 * @date 05/28/2025
 */

@Service
public class MatchService
{
    /*
        Repository Dependencies - SPRING DEPENDENCY INJECTION:
        Need both Job and Contractor repositories to implement matching logic
    */
    private final JobRepository jobRepository;
    private final ContractorRepository contractorRepository;

    /**
     * Constructor for dependency injection of required repositories.
     *
     * @param jobRepository Repository for job database operations
     * @param contractorRepository Repository for contractor database operations
     */

    public MatchService(JobRepository jobRepository, ContractorRepository contractorRepository)
    {
        this.jobRepository = jobRepository;
        this.contractorRepository = contractorRepository;
    }

    /**
     * Matching algorithm that finds and ranks contractors suitable for a specific job.
     *
     * @param jobId The unique identifier of the job to find matches for
     * @return List of ContractorMatchDTO objects ranked by match score (highest first)
     * @throws IllegalArgumentException if job not found with given ID
     */
    public List<ContractorMatchDTO> findMatches(Long jobId)
    {
        // Load job details
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with ID: " + jobId));

        // Get all available contractors from database
        List<Contractor> allContractors = contractorRepository.findAll();

        // Calculate match scores and filter qualified contractors
        List<ContractorMatchDTO> matches = new ArrayList<>();

        for (Contractor contractor : allContractors)
        {
            // Calculate weighted match score using business algorithm
            int score = calculateTotalScore(job, contractor);

            // Only include contractors meeting minimum threshold (35 points)
            if (score >= 35)
            {
                // Create DTO with match details and explanations
                ContractorMatchDTO dto = createMatchDTO(contractor, score, job);
                matches.add(dto);
            }
        }

        //Sort matches by score using QuickSort (highest scores first)
        quickSort(matches, 0, matches.size() - 1);

        return matches;
    }

    /**
     * QuickSort algorithm implementation for sorting contractor matches by score.
     *
     * @param list List of ContractorMatchDTO objects to sort
     * @param low Starting index of the portion to sort
     * @param high Ending index of the portion to sort
     */
    private void quickSort(List<ContractorMatchDTO> list, int low, int high)
    {
        if (low < high)
        {
            // Partition the list and get pivot index
            int pivotIdx = partition(list, low, high);

            // Recursively sort elements before and after partition
            quickSort(list, low, pivotIdx - 1);      // Left partition
            quickSort(list, pivotIdx + 1, high);     // Right partition
        }
    }

    /**
     * Partitioning step of QuickSort algorithm.
     * Rearranges list so elements with scores >= pivot come before the pivot.
     * Modified for descending order sorting (higher scores first).
     *
     * @param list List to partition
     * @param low Starting index of partition range
     * @param high Ending index of partition range (pivot element)
     * @return Final index position of the pivot element
     */
    private int partition(List<ContractorMatchDTO> list, int low, int high)
    {
        // Use last element as pivot
        int pivot = list.get(high).getMatchScore();
        int i = low - 1;

        for (int j = low; j <= high; j++)
        {
            // If current element's score is >= pivot (descending order)
            if (list.get(j).getMatchScore() >= pivot)
            {
                // Swap elements to move higher scores toward the front
                i++;
                ContractorMatchDTO temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        // Place pivot in correct position
        ContractorMatchDTO temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1; // Return pivot's final index
    }

    /**
     * Calculates total weighted match score for a contractor-job pairing.
     * Implements the iRemodel platform's contractor matching algorithm.
     *
     * SCORING FORMULA:
     * Total Score = (Specialty Score × 0.5) + (Proximity Score × 0.3) + (Rating Score × 0.2)
     *
     * BUSINESS WEIGHTS:
     * - Specialty Match: 50% - Most important factor for job success
     * - Location Proximity: 30% - Important for logistics and travel costs
     * - Contractor Rating: 20% - Quality indicator but less weight than relevance
     *
     * @param job The job requiring a contractor
     * @param contractor The contractor being evaluated for the job
     * @return Total weighted score (0-100 scale)
     */
    private int calculateTotalScore(Job job, Contractor contractor)
    {
        // Calculate individual component scores
        double specialtyScore = calculateSpecialtyScore(job.getCategory().name(),
                contractor.getPrimarySpecialty().name());
        double proximityScore = calculateProximityScore(job.getZipCode(), contractor.getZipCode());
        double ratingScore = calculateRatingScore(contractor.getRating());

        // Apply business weights to calculate final score
        double total = (specialtyScore * 0.5) + (proximityScore * 0.3) + (ratingScore * 0.2);
        // Convert to integer for simpler handling
        return (int) total;

    }

    /**
     * Creates a ContractorMatchDTO with match details and explanations.
     * Converts domain entities to API-friendly Data Transfer Objects.
     *
     * @param contractor The contractor entity to convert
     * @param score The calculated match score
     * @param job The job being matched against
     * @return ContractorMatchDTO ready for API response
     */
    private ContractorMatchDTO createMatchDTO(Contractor contractor, int score, Job job)
    {
        // Generate the responses why this contractor matches
        List<String> reasons = generateMatchReasons(job, contractor, score);

        return new ContractorMatchDTO(
                contractor.getId(),
                contractor.getCompanyName(),
                contractor.getPrimarySpecialty().name(),
                contractor.getZipCode(),
                contractor.getRating(),
                score,
                reasons
        );
    }

    /**
     * Calculates specialty match score between job category and contractor expertise.
     *
     * SCORING RULES:
     * - Exact Match: 100 points (contractor specializes in exact job type)
     * - Partial Match: 75 points (specialty contains job category)
     * - No Match: 0 points (unrelated specialties)
     *
     * @param jobCategory The type of work required for the job
     * @param contractorSpecialty The contractor's primary area of expertise
     * @return Specialty match score (0, 75, or 100)
     */
    private double calculateSpecialtyScore(String jobCategory, String contractorSpecialty)
    {
        if (jobCategory.equals(contractorSpecialty))
        {
            return 100.0; // Perfect specialty match
        }
        else if (jobCategory.contains(contractorSpecialty))
        {
            return 75.0; // Partial specialty overlap
        }
        else
        {
            return 0.0; // Partial specialty overlap
        }
    }

    /**
     * Calculates location proximity score between job and contractor locations.
     *
     * SCORING RULES:
     * - Same ZIP Code: 100 points (local contractor, no travel time)
     * - Same Area (first 3 digits): 75 points (nearby, minimal travel)
     * - Different Areas: 0 points (distant, higher travel costs)
     *
     * @param jobZip ZIP code where the job is located
     * @param contractorZip ZIP code where the contractor is based
     * @return Proximity match score (0, 75, or 100)
     */
    private double calculateProximityScore(String jobZip, String contractorZip)
    {
        if (jobZip.equals(contractorZip))
        {
            return 100.0; // Perfect match
        }
        else if (jobZip.length() >= 3 && contractorZip.length() >= 3 &&
                jobZip.substring(0, 3).equals(contractorZip.substring(0, 3)))
        {
            return 75.0; // Same area (first 3 digits match)
        }
        else {
            return 0.0; // No match
        }
    }

    /**
     * Calculates rating-based score for contractor quality assessment.
     *
     * SCORING RULES:
     * - 4.5+ Stars: 100 points (excellent reputation)
     * - 3.5-4.4 Stars: 75 points (good reputation)
     * - 2.5-3.4 Stars: 50 points (average reputation)
     * - Below 2.5 Stars: 25 points (below average reputation)
     * - No Rating: 0 points (unknown quality)
     *
     * @param contractorRating The contractor's average rating (null if no reviews)
     * @return Rating-based score (0, 25, 50, 75, or 100)
     */
    private double calculateRatingScore(Double contractorRating)
    {
        if (contractorRating == null) {
            return 0.0; // No rating available
        } else if (contractorRating >= 4.5) {
            return 100.0; // Excellent rating
        } else if (contractorRating >= 3.5) {
            return 75.0; // Good rating
        } else if (contractorRating >= 2.5) {
            return 50.0; // Average rating
        } else {
            return 25.0; // Below average rating
        }

    }

    /**
     * Generates explanations for why a contractor matches a job.
     * Creates user-friendly match reasons for the response
     *
     * @param job The job being matched
     * @param contractor The contractor being evaluated
     * @param score The calculated match score (unused but available for future logic)
     * @return List of descriptive reasons explaining the match quality
     */
    private List<String> generateMatchReasons(Job job, Contractor contractor, int score)
    {
        List<String> reasons = new ArrayList<>();

        // Check specialty match and add explanation
        if (job.getCategory().name().equals(contractor.getPrimarySpecialty().name())) {
            reasons.add("Specializes in " + job.getCategory().name().toLowerCase() + " projects");
        }

        // Check location proximity and add explanation
        if (job.getZipCode().equals(contractor.getZipCode())) {
            reasons.add("Located in your area");
        } else if (job.getZipCode().length() >= 3 && contractor.getZipCode().length() >= 3 &&
                job.getZipCode().substring(0, 3).equals(contractor.getZipCode().substring(0, 3))) {
            reasons.add("Located nearby");
        }

        // Check rating and add explanation
        if (contractor.getRating() != null && contractor.getRating() >= 4.5) {
            reasons.add("Highly rated with " + contractor.getRating() + " stars");
        } else if (contractor.getRating() != null && contractor.getRating() >= 3.5) {
            reasons.add("Good rating with " + contractor.getRating() + " stars");
        } else if (contractor.getRating() != null && contractor.getRating() >= 2.5) {
            reasons.add("Average rating with " + contractor.getRating() + " stars");
        } else {
            reasons.add("Below average rating with " + contractor.getRating() + " stars");
        }
        return reasons;
    }

}
