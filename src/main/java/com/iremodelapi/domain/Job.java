package com.iremodelapi.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a home improvement job posted by a homeowner in the iRemodel platform.
 * Manages job details, contractor assignments, status tracking, and associated reviews.
 * Includes business logic for job lifecycle management and contractor assignment validation.
 *
 * @author [Your Name]
 * @date [Current Date]
 */

//Marks this class as a JPA entity, will be mapped to a database table
@Entity
//specifies the table name in the database
@Table(name = "jobs")
public class Job
{
    //@Id marks this field as the primary key for the entity
    @Id

    //@GeneratedValue with IDENTITY strategy lets database auto-generate the ID values
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column with nullable = false creates a NOT NULL constraint in the database
    @Column(nullable = false)
    private String title;

    //columnDefinition = "TEXT" creates a TEXT column for longer descriptions
    @Column(columnDefinition = "TEXT")
    private String description;

    //@Enumerated(EnumType.STRING) stores enum values as strings in database
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobCategory category;

    //Stores the job location as a zip code
    @Column(nullable = false)
    private String zipCode;

    /*
        Financial Precision -
        BigDecimal is used instead of double/float for financial values to avoid
        floating-point precision errors. precision=10 allows up to 10 digits total,
        scale=2 means 2 digits after decimal point (perfect for currency: $99999999.99)
        This ensures accuracy and consistency in financial calculations,
        I was using Double before but was recommended to use BigDecimal by my code editor

    */
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal budget;

    //Job status with default value of OPEN
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.OPEN;

    /*
        Timestamp Management - JAVA TIME API: updatable = false
        prevents the createdAt timestamp from being modified after initial creation.
     */
    @Column (nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /*
        JPA Relationships - HIBERNATE/JPA FEATURES (Beyond Basic Java):

        @ManyToOne: Many jobs can belong to one homeowner
        @JoinColumn: Specifies the foreign key column name in the jobs table
        FetchType.LAZY: Homeowner data is only loaded when explicitly accessed
        nullable = false: Every job must have a homeowner
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeowner_id", nullable = false)
    private Homeowner homeowner;

    //Many jobs can be assigned to one contractor (contractor_id can be null for unassigned jobs)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id")
    private Contractor assignedContractor;

    /*
        Advanced JPA Relationship Features:
        @OneToMany: One job can have many reviews
        cascade = CascadeType.ALL: When job is saved/updated/deleted, do same to reviews
        orphanRemoval = true: If review is removed from this list, delete it from database
        mappedBy = "job": Reviews entity has a "job" field that owns this relationship
    */
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // Enums - Basic Java feature defining fixed sets of constants
    public enum JobStatus
    {
        OPEN,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    public enum JobCategory
    {
        PLUMBING,           // Water systems, pipes, fixtures, drains
        ELECTRICAL,         // Wiring, electrical panels, lighting fixtures
        HVAC,               // Heating, ventilation, air conditioning systems
        ROOFING,            // Roof installation, repair, replacement
        PAINTING,           // Interior/exterior painting, staining, finishing
        CARPENTRY,          // Woodworking, framing, trim, custom built-ins
        LANDSCAPING,        // Yard design, planting, hardscaping, irrigation
        FLOORING,           // Hardwood, laminate, tile, carpet installation
        REMODELING,         // Comprehensive home renovations
        GENERAL,            // Broad range of construction services
        MASONRY,            // Brick, stone, concrete block structures
        DRYWALL,            // Wall installation, repair, texturing
        TILE,               // Ceramic, porcelain, natural stone installation
        CABINETRY,          // Custom cabinets, shelving, storage solutions
        COUNTERTOPS,        // Kitchen/bath counters in various materials
        DECKS,              // Outdoor deck construction and restoration
        WINDOWS,            // Window installation, repair, replacement
        DOORS,              // Interior/exterior door installation and repair
        SIDING,             // Exterior wall cladding installation
        CONCRETE,           // Foundations, driveways, patios, walkways
        DEMOLITION,         // Structure removal, interior demolition
        INSULATION,         // Thermal and acoustic insulation installation
        FENCING,            // Property boundary fences and gates
        BASEMENT,           // Basement finishing and waterproofing
        HOME_ADDITION,      // Room additions, second stories, expansions
        GARAGE,             // Garage construction, conversion, door repair
        GUTTERS,            // Rain gutter installation and maintenance
        SOLAR               // Solar panel systems installation and service
    }

    /**
     * Default constructor for JPA entity creation and framework usage.
     */
    public Job()
    {
        // Default constructor required by JPA
    }

    /**
     * Creates a new Job instance with required information.
     * Sets initial status to OPEN and timestamps will be set by JPA lifecycle methods.
     *
     * @param title job title/summary
     * @param description detailed job description
     * @param category job category/type
     * @param zipCode job location zip code
     * @param budget job budget amount
     * @param homeowner homeowner posting the job
     */
    public Job(String title, String description, JobCategory category, String zipCode, BigDecimal budget,
               Homeowner homeowner)
    {
        this.title = title;
        this.description = description;
        this.category = category;
        this.zipCode = zipCode;
        this.budget = budget;
        this.homeowner = homeowner;
        // status defaults to OPEN, timestamps set by @PrePersist
    }

    // Getters and Setters
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public JobCategory getCategory()
    {
        return category;
    }

    public void setCategory(JobCategory category)
    {
        this.category = category;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public BigDecimal getBudget()
    {
        return budget;
    }

    public void setBudget(BigDecimal budget)
    {
        this.budget = budget;
    }

    public JobStatus getStatus()
    {
        return status;
    }

    public void setStatus(JobStatus status)
    {
        this.status = status;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Homeowner getHomeowner()
    {
        return homeowner;
    }

    public void setHomeowner(Homeowner homeowner)
    {
        this.homeowner = homeowner;
    }

    public Contractor getAssignedContractor()
    {
        return assignedContractor;
    }

    public void setAssignedContractor(Contractor assignedContractor)
    {
        this.assignedContractor = assignedContractor;
    }

    /**
     * Returns all reviews associated with this job.
     *
     * @return List of job reviews
     */
    public List<Review> getReviews()
    {
        return reviews;
    }

    public void setReviews(List<Review> reviews)
    {
        this.reviews = reviews;
    }

    /**
     * Adds a review to this job and establishes bidirectional relationship.
     *
     * @param review the review to add to this job
     */
    public void addReview(Review review)
    {
        reviews.add(review);
        review.setJob(this);
    }

    public void removeReview(Review review)
    {
        reviews.remove(review);
        review.setJob(null);
    }

    /**
     * Checks if this job is available for contractor assignment.
     *
     * @return true if job status is OPEN, false otherwise
     */
    public boolean isOpen()
    {
        return this.status == JobStatus.OPEN;
    }

    /**
     * Checks if this job has been completed.
     *
     * @return true if job status is COMPLETED, false otherwise
     */
    public boolean isCompleted()
    {
        return this.status == JobStatus.COMPLETED;
    }

    /**
     * Validates whether a contractor can be assigned to this job.
     * Job must be OPEN status and contractor must not be null.
     *
     * @param contractor the contractor to validate for assignment
     * @return true if contractor can be assigned, false otherwise
     */
    public boolean canBeAssignedTo(Contractor contractor)
    {
        return this.isOpen() && contractor != null;
    }

    /**
     * Assigns a contractor to this job and updates status to IN_PROGRESS.
     * Validates assignment is allowed before making changes.
     *
     * @param contractor the contractor to assign to this job
     * @throws IllegalStateException if job cannot be assigned to contractor
     */
    public void assignContractor(Contractor contractor)
    {
        if (!canBeAssignedTo(contractor))
        {
            throw new IllegalStateException("Job cannot be assigned to this contractor");
        }
        this.assignedContractor = contractor;
        this.status = JobStatus.IN_PROGRESS;
        //updatedAt will be set automatically by @PreUpdate
    }

    /**
     * Marks this job as completed.
     * Only jobs with IN_PROGRESS status can be marked as completed.
     *
     * @throws IllegalStateException if job is not currently in progress
     */
    public void markCompleted()
    {
        if (this.status != JobStatus.IN_PROGRESS)
        {
            throw new IllegalStateException("Only in-progress jobs can be marked as completed");
        }
        this.status = JobStatus.COMPLETED;
        //updatedAt will be set automatically by @PreUpdate
    }

    @Override
    public String toString()
    {
        return "Job:\n" +
                "  id: " + id + "\n" +
                "  title: " + title + "\n" +
                "  description: " + description + "\n" +
                "  category: " + category + "\n" +
                "  zipCode: " + zipCode + "\n" +
                "  budget: " + budget + "\n" +
                "  status: " + status + "\n" +
                "  createdAt: " + createdAt + "\n" +
                "  updatedAt: " + updatedAt;
    }

    /**
     * Determines equality for Job entities based on business logic rather than database IDs.
     *
     * Business Rule: A job is considered unique by the combination of title, homeowner,
     * and creation timestamp. This prevents duplicate job postings where the same homeowner
     * creates multiple identical jobs, ensuring clean data and avoiding confusion for contractors.
     *
     * Implementation Logic:
     * - title: Prevents duplicate job names from same homeowner
     * - homeowner: Ensures uniqueness is scoped to individual homeowners
     * - createdAt: Handles edge case where homeowner might legitimately post same job type later
     *
     *  Reference check (this == o) for performance optimization
     *  null and class type validation for safety
     *  Business field combination comparison for uniqueness logic
     *
     * Note: Follows JPA entity equality best practices for Hibernate/Spring Data JPA.
     * AI assistance was used to understand proper entity equality patterns and
     * business logic implementation for preventing duplicate job postings.
     *
     * @param o the object to compare with this Job
     * @return true if the jobs represent the same title/homeowner/creation time combination
     */
    @Override
    public boolean equals(Object o) {
        // Check if same reference
        if (this == o) return true;

        // Check if null or different class
        if (o == null || getClass() != o.getClass()) return false;

        // Cast to Job and compare unique combination of fields
        Job job = (Job) o;
        return Objects.equals(title, job.title) &&
                Objects.equals(homeowner, job.homeowner) &&
                Objects.equals(createdAt, job.createdAt);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(title, homeowner, createdAt);
    }

    /**
     * JPA lifecycle method - automatically called before entity is saved to database.
     * Sets both createdAt and updatedAt timestamps to current time.
     */
    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * JPA lifecycle method - automatically called before entity is updated in the database.
     * Updates the updatedAt timestamp to current time.
     */
    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt = LocalDateTime.now();
    }

}
