package com.iremodelapi.web.dto;

import java.util.List;
/**
 * Data Transfer Object (DTO) for contractor matching algorithm results.
 * Represents the output of the sophisticated contractor-to-job matching system,
 * combining source contractor data with computed algorithm results and user explanations.
 *
 * ALGORITHM INTEGRATION:
 * This DTO represents the final output of the complete matching workflow:
 * 1. Weighted scoring algorithm (specialty 50%, location 30%, rating 20%)
 * 2. Threshold filtering (minimum 35 points to qualify)
 * 3. QuickSort ranking (O(n log n) complexity for sorting by match score)
 * 4. Human-readable explanation generation
 * 5. API-optimized data structure for frontend consumption
 *
 * DATA COMPOSITION:
 * Combines three types of information:
 * - Source Data: Basic contractor information from database
 * - Computed Data: Algorithm-generated match scores and rankings
 * - UX Data: Human-readable explanations for match quality
 *
 * API CONTRACT:
 * Defines the response format for GET /api/jobs/{jobId}/matches endpoint.
 * Optimized for frontend applications displaying contractor recommendations.
 *
 * @author Tyson Ringelstetter
 * @date 05/28/2025
 */
/*
    SOPHISTICATED DTO PATTERN - ALGORITHM OUTPUT REPRESENTATION:

    This DTO demonstrates advanced DTO usage beyond simple data transfer:
    - Combines source data + computed results + user experience enhancements
    - Represents complex algorithm output in client-friendly format
    - Bridges CS algorithms (QuickSort) with professional API design
    - Optimized for rich frontend user experiences

*/
public class ContractorMatchDTO
{
    private Long contractorId;
    private String contractorName;
    private String specialty;
    private String zipCode;
    private double rating;
    private int matchScore;
    private List<String> matchReasons;

    public ContractorMatchDTO()
    {
        // Default constructor
    }

    /**
     * Parameterized constructor for creating populated contractor match results.
     * Used by MatchService to create DTOs with algorithm output and contractor data.
     *
     * @param contractorId Unique identifier of the matched contractor
     * @param contractorName Company/business name of the contractor
     * @param specialty Primary area of expertise (converted from enum to string)
     * @param zipCode Contractor's base location
     * @param rating Average customer rating from past reviews
     * @param matchScore Computed algorithm score (0-100 scale)
     * @param matchReasons List of human-readable match explanations
     */
    public ContractorMatchDTO(Long contractorId, String contractorName, String specialty, String zipCode,
                              double rating, int matchScore, List<String> matchReasons)
    {
        this.contractorId = contractorId;
        this.contractorName = contractorName;
        this.specialty = specialty;
        this.zipCode = zipCode;
        this.rating = rating;
        this.matchScore = matchScore;
        this.matchReasons = matchReasons;
    }

    public Long getContractorId()
    {
        return contractorId;
    }

    public void setContractorId(Long contractorId)
    {
        this.contractorId = contractorId;
    }

    public String getContractorName()
    {
        return contractorName;
    }

    public void setContractorName(String contractorName)
    {
        this.contractorName = contractorName;
    }

    public String getSpecialty()
    {
        return specialty;
    }

    public void setSpecialty(String specialty)
    {
        this.specialty = specialty;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public double getRating()
    {
        return rating;
    }

    public void setRating(double rating)
    {
        this.rating = rating;
    }

    /**
     * Gets the computed match score from the algorithm.
     *
     * @return Match score (0-100 scale) computed by weighted algorithm
     */
    public int getMatchScore()
    {
        return matchScore;
    }

    public void setMatchScore(int matchScore)
    {
        this.matchScore = matchScore;
    }

    public List<String> getMatchReasons()
    {
        return matchReasons;
    }

    public void setMatchReasons(List<String> matchReasons)
    {
        this.matchReasons = matchReasons;
    }

    @Override
    public String toString() {
        return "ContractorMatchDTO {\n" +
                "  contractorId: " + contractorId + "\n" +
                "  contractorName: '" + contractorName + "'\n" +
                "  specialty: '" + specialty + "'\n" +
                "  zipCode: '" + zipCode + "'\n" +
                "  rating: " + rating + "\n" +
                "  matchScore: " + matchScore + "\n" +
                "  matchReasons: " + matchReasons + "\n" +
                "}";
    }

}
