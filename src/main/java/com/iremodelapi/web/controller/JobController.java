package com.iremodelapi.web.controller;

import com.iremodelapi.domain.Job;
import com.iremodelapi.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController
{
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    // Define endpoints for job operations here
    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{jobId}")
    public Job getJobById(@PathVariable Long jobId) {
        return jobService.findJobById(jobId);
    }

    @GetMapping("/search")
    public List<Job> searchJobsByZipCode(@RequestParam String zipCode) {
        return jobService.findJobsByZipCode(zipCode);
    }

    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    @PutMapping("/{jobId}/complete")
    public Job completeJob(@PathVariable Long jobId) {
        return jobService.completeJob(jobId);
    }

    @PutMapping("/{jobId}/assign/{contractorId}")
    public Job assignContractorToJob(@PathVariable Long jobId, @PathVariable Long contractorId)
    {
        return jobService.assignContractorToJob(jobId, contractorId);
    }

}
