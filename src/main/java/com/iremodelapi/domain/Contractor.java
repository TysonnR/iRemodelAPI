package com.iremodelapi.domain;

import jakarta.persistence.Entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name= "contractors")
@PrimaryKeyJoinColumn(name="user_id")

public class Contractor extends User
{
    //Contractor specific attributes
    @Column(name="company_name", nullable = false)
    private String companyName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String address;

    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    @Column
    private boolean insured;

    // Performance tracking attributes
    @Column
    private Double rating;

    @Column(name= "completed_jobs_count")
    private Integer completedJobsCount = 0;

    //Collections
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "contractor_service_areas",
            joinColumns = @JoinColumn(name = "contractor_id")
    )
    @Column(name = "zip_code")
    private Set<String> serviceAreas = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "contractor_specialties",
            joinColumns = @JoinColumn(name = "contractor_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "specialty")
    private Set<Specialty> specialties = new HashSet<>();

    //Map relationship to jobs
    @OneToMany(mappedBy = "assignedContractor", fetch = FetchType.LAZY)
    private List<Job> assignedJobs = new ArrayList<>();

    // Map relationship to reviews
    @OneToMany(mappedBy = "contractor", fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    public enum Specialty
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

    public Contractor()
    {
        // Default constructor
        super();
    }

    public Contractor(String email, String password, String fullName, String phoneNumber, String companyName,
                      String description, String address, String licenseNumber, boolean insured)
    {
        super(email, password, fullName, phoneNumber, UserRole.CONTRACTOR);
        this.companyName = companyName;
        this.description = description;
        this.address = address;
        this.licenseNumber = licenseNumber;
        this.insured = insured;
        this.rating = null;
        this.completedJobsCount = 0;
    }

    // Getters and Setters for each attribute
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getLicenseNumber()
    {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber)
    {
        this.licenseNumber = licenseNumber;
    }

    public boolean isInsured()
    {
        return insured;
    }

    public void setInsured(boolean insured)
    {
        this.insured = insured;
    }

    public Double getRating()
    {
        return rating;
    }

    public void setRating(Double rating)
    {
        this.rating = rating;
    }

    public Integer getCompletedJobsCount()
    {
        return completedJobsCount;
    }

    public void setCompletedJobsCount(Integer completedJobsCount)
    {
        this.completedJobsCount = completedJobsCount;
    }

    public Set<String> getServiceAreas()
    {
        return serviceAreas;
    }

    public void setServiceAreas(Set<String> serviceAreas)
    {
        this.serviceAreas = serviceAreas;
    }

    public Set<Specialty> getSpecialties()
    {
        return specialties;
    }

    public void setSpecialties(Set<Specialty> specialties)
    {
        this.specialties = specialties;
    }

    public List<Job> getAssignedJobs()
    {
        return assignedJobs;
    }

    public void setAssignedJobs(List<Job> assignedJobs)
    {
        this.assignedJobs = assignedJobs;
    }

    public List<Review> getReviews()
    {
        return reviews;
    }

    public void setReviews(List<Review> reviews)
    {
        this.reviews = reviews;
    }

    //Helper methods for relationship management
    public void addJob(Job job)
    {
        assignedJobs.add(job);
        job.setAssignedContractor(this);
    }

    public void removeJob(Job job)
    {
        assignedJobs.remove(job);
        job.setAssignedContractor(null);
    }

    public void addReview(Review review)
    {
        reviews.add(review);
        review.setContractor(this);
    }

    // Business logic methods
    public void addSpecialty(Specialty specialty)
    {
        this.specialties.add(specialty);
    }

    public void removeSpecialty(Specialty specialty)
    {
        this.specialties.remove(specialty);
    }

    public void addServiceArea(String serviceArea)
    {
        this.serviceAreas.add(serviceArea);
    }

    public void incrementCompletedJobsCount()
    {
        if (this.completedJobsCount == null)
        {
            this.completedJobsCount = 0;
        }
        else {
            this.completedJobsCount++;
        }
    }

    public void updateRatingFromReviews()
    {
        if (reviews.isEmpty()) {
            this.rating = null;
            return;
        }

        double averageRating = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }

    public boolean servicesArea(String zipCode)
    {
        return serviceAreas.contains(zipCode);
    }

    public boolean hasSpecialty(Specialty specialty)
    {
        return specialties.contains(specialty);
    }

    public Specialty getPrimarySpecialty()
    {
        return specialties.isEmpty() ? null : specialties.iterator().next();
    }

    @Override
    public String toString()
    {
        return "Contractor{" +
                "companyName='" + companyName + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", insured=" + insured +
                ", rating=" + rating +
                ", completedJobsCount=" + completedJobsCount +
                ", serviceAreas=" + serviceAreas +
                ", specialties=" + specialties +
                ", assignedJobs=" + (assignedJobs != null ? assignedJobs.size() : 0) +
                ", reviews=" + (reviews != null ? reviews.size() : 0) +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Contractor contractor)) return false;

        return Objects.equals(getEmail(), contractor.getEmail());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getEmail());
    }

}
