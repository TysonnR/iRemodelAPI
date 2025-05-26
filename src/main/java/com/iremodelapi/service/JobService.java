package com.iremodelapi.service;

import com.iremodelapi.domain.Contractor;
import com.iremodelapi.domain.Job;
import com.iremodelapi.repository.ContractorRepository;
import com.iremodelapi.repository.HomeownerRepository;
import com.iremodelapi.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class JobService
{
    // Declaring dependencies as private final fields

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

    public Job assignContractorToJob(Long jobId, Long contractorId)
    {

        Job job = findJobById(jobId);
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found with id: " + contractorId));

        job.assignContractor(contractor);
        contractor.addJob(job);

        return jobRepository.save(job);

    }

    public List<Job> getAllJobs()
    {
        return jobRepository.findAll();
    }

    public List<Job> findJobsByZipCode(String zipCode)
    {
        return jobRepository.findByZipCode(zipCode);
    }

    public List<Job> findJobsByHomeownerId(Long homeownerId)
    {
        return jobRepository.findByHomeownerId(homeownerId);
    }

    public List<Job> findJobsByContractor(Long contractorId)
    {
        return jobRepository.findByAssignedContractorId(contractorId);
    }

    public Job completeJob(Long jobId)
    {
        Job job = findJobById(jobId);

        job.markCompleted();

        return jobRepository.save(job);
    }
}
