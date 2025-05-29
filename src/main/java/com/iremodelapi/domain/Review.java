package com.iremodelapi.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a customer review in the iRemodel platform.
 * Links a homeowner's rating and feedback to a specific contractor for a completed job.
 * Provides the foundation for contractor rating calculations and reputation tracking.
 *
 * @author [Your Name]
 * @date 5/28/2025
 */
//Marks this class as a JPA entity, will be mapped to a database table
@Entity
//Specifies the table name in the database
@Table(name = "reviews")
public class Review
{
    //@Id marks this field as the primary key for the entity
    @Id
    //@GeneratedValue with IDENTITY strategy lets the database auto-generate ID values
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Rating value (1-5 stars) cannot be null
    @Column(nullable = false)
    private Integer rating;

    //Review comment/feedback text, uses TEXT column for longer comments
    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    /*
     Timestamp Management - JAVA TIME API: updatable = false
     prevents the createdAt timestamp from being modified after initial creation.
    */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /*
        JPA Relationships - HIBERNATE/JPA FEATURES (Beyond Basic Java):

        All three relationships are @ManyToOne because:
        - Many reviews can be for one job
        - Many reviews can be written by one homeowner
        - Many reviews can be about one contractor

        FetchType.LAZY: Related entities are only loaded when specifically accessed
        nullable = false: Every review must have a job, homeowner, and contractor
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeowner_id", nullable = false)
    private Homeowner homeowner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id", nullable = false)
    private Contractor contractor;

    /**
     * Default constructor for JPA entity creation and framework usage.
     */
    public Review()
    {
        // Default constructor
    }

    /**
     * Creates a new Review instance with all its required information.
     * The createdAt timestamp will be set automatically by JPA lifecycle method
     *
     * @param rating numerical rating (1-5)
     * @param comment written feedback/review text
     * @param job the job this review is for
     * @param homeowner the homeowner writing the review
     * @param contractor the contractor being reviewed
     */
    public Review(Integer rating, String comment, Job job, Homeowner homeowner, Contractor contractor)
    {
        this.rating = rating;
        this.comment = comment;
        this.job = job;
        this.homeowner = homeowner;
        this.contractor = contractor;
        // createdAt will be set automatically by @PrePersist
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
    /**
     * Determines equality for Review entities based on business logic rather than database IDs.
     *
     * Business Rule: A review is considered unique by the combination of homeowner, contractor,
     * and job. This prevents duplicate reviews where the same homeowner reviews the same
     * contractor for the same job multiple times, ensuring data integrity and authentic ratings.
     *
     * Implementation Notes:
     * - Uses Objects.equals() for null-safe comparison of entity relationships
     * - Follows JPA best practices for entity equality in Hibernate/Spring Data
     * - Supports proper Set operations and prevents duplicates
     *
     *  Reference check (this == o) for performance optimization
     *  Null and class type validation for safety
     *  Business relationship comparison for uniqueness logic
     *
     *  Note: AI assistance was used to understand proper JPA entity equality patterns
     *  and business logic implementation for preventing duplicate reviews.
     */

    public boolean equals(Object o)
    {
        // Check if same reference
        if (this == o) return true;

        // Check if null or different class
        if (o == null || getClass() != o.getClass()) return false;

        // Cast to Review and compare unique combination of relationships
        // A review is unique by the combination of homeowner, contractor, and job
        Review review = (Review) o;
        return Objects.equals(homeowner, review.homeowner) &&
                Objects.equals(contractor, review.contractor) &&
                Objects.equals(job, review.job);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(homeowner, contractor, job);
    }

    @Override
    public String toString()
    {
        return "Review:\n" +
                "  id: " + id + "\n" +
                "  rating: " + rating + "\n" +
                "  comment: " + comment + "\n" +
                "  createdAt: " + createdAt + "\n" +
                // Show related entity IDs to avoid loading full objects (performance)
                "  jobId: " + (job != null ? job.getId() : "null") + "\n" +
                "  homeownerId: " + (homeowner != null ? homeowner.getId() : "null") + "\n" +
                "  contractorId: " + (contractor != null ? contractor.getId() : "null");
    }

    /**
     * JPA lifecycle method - automatically called before entity is saved to database.
     * Sets the createdAt timestamp to current time.
     */
    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
    }


}