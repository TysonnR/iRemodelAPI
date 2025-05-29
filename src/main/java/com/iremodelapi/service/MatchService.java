package com.iremodelapi.service;

import com.iremodelapi.domain.Contractor;
import com.iremodelapi.domain.Job;
import com.iremodelapi.repository.ContractorRepository;
import com.iremodelapi.repository.JobRepository;
import com.iremodelapi.web.dto.ContractorMatchDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService
{
    private final JobRepository jobRepository;
    private final ContractorRepository contractorRepository;

    public MatchService(JobRepository jobRepository, ContractorRepository contractorRepository)
    {
        this.jobRepository = jobRepository;
        this.contractorRepository = contractorRepository;
    }

    public List<ContractorMatchDTO> findMatches(Long jobId)
    {
        // Step 1: Get the job (what if jobId doesn't exist?)
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with ID: " + jobId));
        // Step 2: Get all contractors
        List<Contractor> allContractors = contractorRepository.findAll();
        // Step 3: Score each contractor using your formula
        List<ContractorMatchDTO> matches = new ArrayList<>();

        for (Contractor contractor : allContractors)
        {
            int score = calculateTotalScore(job, contractor);

            if (score >= 35)
            {
                ContractorMatchDTO dto = createMatchDTO(contractor, score, job);
                matches.add(dto);
            }
        }
        // Step 5: Sort by score (highest first)
        quickSort(matches, 0, matches.size() - 1);
        return matches;
        // Step 6: Convert to DTOs with match reasons
        // Step 7: Return the list
    }

    private void quickSort(List<ContractorMatchDTO> list, int low, int high)
    {
        if (low < high)
        {
            int pivotIdx = partition(list, low, high);
            quickSort(list, low, pivotIdx - 1);
            quickSort(list, pivotIdx + 1, high);
        }
    }

    private int partition(List<ContractorMatchDTO> list, int low, int high)
    {
        int pivot = list.get(high).getMatchScore();
        int i = low - 1;

        for (int j = low; j <= high; j++)
        {
            if (list.get(j).getMatchScore() >= pivot)
            {
                // Swap elements
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

        return i + 1;
    }

    private int calculateTotalScore(Job job, Contractor contractor)
    {
        // Your formula: (specialtyScore * 0.5) + (proximityScore * 0.3) + (ratingScore * 0.2)

        double specialtyScore = calculateSpecialtyScore(job.getCategory().name(),
                contractor.getPrimarySpecialty().name());
        double proximityScore = calculateProximityScore(job.getZipCode(), contractor.getZipCode());
        double ratingScore = calculateRatingScore(contractor.getRating());

        // Apply your weights and return total
        double total = (specialtyScore * 0.5) + (proximityScore * 0.3) + (ratingScore * 0.2);
        return (int) total;

    }

    private ContractorMatchDTO createMatchDTO(Contractor contractor, int score, Job job)
    {
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

    private double calculateSpecialtyScore(String jobCategory, String contractorSpecialty)
    {
        if (jobCategory.equals(contractorSpecialty))
        {
            return 100.0; // Perfect match
        }
        else if (jobCategory.contains(contractorSpecialty))
        {
            return 75.0; // Partial match
        }
        else
        {
            return 0.0; // No match
        }
    }

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
    private List<String> generateMatchReasons(Job job, Contractor contractor, int score)
    {
        List<String> reasons = new ArrayList<>();

        //check specialty match
        if (job.getCategory().name().equals(contractor.getPrimarySpecialty().name())) {
            reasons.add("Specializes in " + job.getCategory().name().toLowerCase() + " projects");
        }

        // Check location proximity
        if (job.getZipCode().equals(contractor.getZipCode())) {
            reasons.add("Located in your area");
        } else if (job.getZipCode().length() >= 3 && contractor.getZipCode().length() >= 3 &&
                job.getZipCode().substring(0, 3).equals(contractor.getZipCode().substring(0, 3))) {
            reasons.add("Located nearby");
        }

        // Check rating
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
