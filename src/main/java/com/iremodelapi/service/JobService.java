package com.iremodelapi.service;

import com.iremodelapi.domain.Job;
import com.iremodelapi.repository.ContractorRepository;
import com.iremodelapi.repository.HomeownerRepository;
import com.iremodelapi.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class JobService
{
    // Declaring dependecies as private final fields

    private final JobRepository jobRepository;
    private final ContractorRepository contractorRepository;
    private final HomeownerRepository homeownerRepository;

    // Constructor to inject dependencies
    public JobService(JobRepository jobRepository, ContractorRepository contractorRepository, HomeownerRepository homeownerRepository)
    {
        this.jobRepository = jobRepository;
        this.contractorRepository = contractorRepository;
        this.homeownerRepository = homeownerRepository;
    }

    public Job findJobById(Long jobId)
    {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
    }

    public Job createJob(Job job)
    {
        if (job.getBudget() == null || job.getBudget().compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new IllegalArgumentException("Budget must be greater than zero");
        }

        if (job.getTitle() == null || job.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Job title is required");
        }

        if (job.getHomeowner() == null)
        {
            throw new IllegalArgumentException("Homeowner is required");
        }

        if (job.getStatus() == null)
        {
            job.setStatus(Job.JobStatus.OPEN);
        }
        return jobRepository.save(job);
    }

    public assignContractorToJob(Long jobId, Long contractorId)
    {

    }
}
