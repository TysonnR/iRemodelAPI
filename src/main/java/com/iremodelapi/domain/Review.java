package com.iremodelapi.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reviews")
public class Review
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeowner_id", nullable = false)
    private Homeowner homeowner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id", nullable = false)
    private Contractor contractor;

    public Review()
    {
        // Default constructor
    }

    public Review(Integer rating, String comment, Job job, Homeowner homeowner, Contractor contractor)
    {
        this.rating = rating;
        this.comment = comment;
        this.job = job;
        this.homeowner = homeowner;
        this.contractor = contractor;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getRating()
    {
        return rating;
    }

    public void setRating(Integer rating)
    {
        this.rating = rating;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public Job getJob()
    {
        return job;
    }

    public void setJob(Job job)
    {
        this.job = job;
    }

    public Homeowner getHomeowner()
    {
        return homeowner;
    }

    public void setHomeowner(Homeowner homeowner)
    {
        this.homeowner = homeowner;
    }

    public Contractor getContractor()
    {
        return contractor;
    }

    public void setContractor(Contractor contractor)
    {
        this.contractor = contractor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;

        return Objects.equals(homeowner, review.homeowner) &&
                Objects.equals(contractor, review.contractor) &&
                Objects.equals(job, review.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeowner, contractor, job);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", job=" + (job != null ? job.getId() : null) +
                ", homeowner=" + (homeowner != null ? homeowner.getId() : null) +
                ", contractor=" + (contractor != null ? contractor.getId() : null) +
                '}';
    }
    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
    }


}