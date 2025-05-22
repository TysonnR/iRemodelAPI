package com.iremodelapi.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "jobs")
public class Job
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobCategory category;

    @Column(nullable = false)
    private String zipCode;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal budget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.OPEN;

    @Column (nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeowner_id", nullable = false)
    private Homeowner homeowner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id")
    private Contractor assignedContractor;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // Enums
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

    // Default constructor (required by JPA)
    public Job()
    {
        // Default constructor
    }

    public Job(String title, String description, JobCategory category, String zipCode, BigDecimal budget,
               Homeowner homeowner)
    {
        this.title = title;
        this.description = description;
        this.category = category;
        this.zipCode = zipCode;
        this.budget = budget;
        this.homeowner = homeowner;
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

    public List<Review> getReviews()
    {
        return reviews;
    }

    public void setReviews(List<Review> reviews)
    {
        this.reviews = reviews;
    }

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

    // Business logic methods
    public boolean isOpen() {
        return this.status == JobStatus.OPEN;
    }

    public boolean isCompleted() {
        return this.status == JobStatus.COMPLETED;
    }

    public boolean canBeAssignedTo(Contractor contractor) {
        return this.isOpen() && contractor != null;
    }

    public void assignContractor(Contractor contractor) {
        if (!canBeAssignedTo(contractor)) {
            throw new IllegalStateException("Job cannot be assigned to this contractor");
        }
        this.assignedContractor = contractor;
        this.status = JobStatus.IN_PROGRESS;
    }

    public void markCompleted() {
        if (this.status != JobStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only in-progress jobs can be marked as completed");
        }
        this.status = JobStatus.COMPLETED;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", zipCode='" + zipCode + '\'' +
                ", budget=" + budget +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Job job)) return false;

        return Objects.equals(title, job.title) &&
                Objects.equals(homeowner, job.homeowner) &&
                Objects.equals(createdAt, job.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, homeowner, createdAt);
    }

    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt = LocalDateTime.now();
    }

}
