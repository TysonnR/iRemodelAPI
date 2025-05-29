package com.iremodelapi.web.dto;

import java.util.List;

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
    public String toString()
    {
        return "ContractorMatchDTO{" +
                "contractorId=" + contractorId +
                ", contractorName='" + contractorName + '\'' +
                ", specialty='" + specialty + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", rating=" + rating +
                ", matchScore=" + matchScore +
                ", matchReasons=" + matchReasons +
                '}';
    }

}
