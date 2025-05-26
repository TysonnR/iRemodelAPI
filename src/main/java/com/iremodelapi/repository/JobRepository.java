package com.iremodelapi.repository;

import com.iremodelapi.domain.Homeowner;
import com.iremodelapi.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>
{
    // Find jobs by homeowner's zip code
    List<Job> findByZipCode(String zipCode);

    // Find jobs by job status
    List<Job> findByStatus(Job.JobStatus status);

    // Find jobs by job category
    List<Job> findByCategory(Job.JobCategory category);

    // Find jobs by homeowner
    List<Job> findByHomeownerId(Long homeownerId);

    List<Job> findByAssignedContractorId(Long contractorId);


}

