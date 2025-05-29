package com.iremodelapi.domain;

import jakarta.persistence.Entity;

import jakarta.persistence.*;

import java.util.*;

/**
 * Represents a contractor in the iRemodel platform.
 * Extends User to inherit basic user information and adds contractor-specific attributes.
 * Manages contractor specialties, service areas, job assignments, and performance metrics.
 *
 * @author Tyson Ringelstetter
 * @date 5/27/2025
 */

//Marks this class as a JPA entity, will be mapped to a database table
@Entity
//Specifies the table name in the database
@Table(name= "contractors")
/*  Defines the primary key column for inheritance mapping when using JOINED table strategy.
    this tells JPA that the contractors table shares its primary key with the users table through
    the user_id foreign key.
*/
@PrimaryKeyJoinColumn(name="user_id")

public class Contractor extends User
{
    //Contractor specific attributes
    //@Column maps the attribute to a database column, name specifies the column name, creates a non-null constraint
    @Column(name="company_name", nullable = false)
    private String companyName;

    // defines the SQL column type for longer a description
    @Column(columnDefinition = "TEXT")
    private String description;

    // Defines the address column, can be null if not provided
    @Column
    private String address;

    // Defines the license number column, unique constraint ensures no two contractors can have the same license number
    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    // Defines the insured column, indicates if the contractor is insured
    @Column
    private boolean insured;

    // Performance tracking attributes
    @Column
    private Double rating;

    //Defines the completed jobs count column, initializes to 0
    @Column(name= "completed_jobs_count")
    private Integer completedJobsCount = 0;

    /*
        Collection Mappings - HIBERNATE/JPA FEATURES (Beyond Basic Java):

         *** HIBERNATE/JPA ANNOTATIONS (Database ORM Magic):
            -@ElementCollection: Maps a collection of basic types (zip codes) to a separate table
            -@CollectionTable: Defines the join table for storing the collection elements
            -@JoinColumn: Specifies the foreign key column that links to the Contractor entity
            -@Enumerated(EnumType.STRING): Maps the enum values as strings in the database.
                Tells the database to store enum values as strings instead of numbers.
                * This is safer because adding new enum values won't break existing data.
            -@OneToMany: Defines a one-to-many relationship (One contractor can have many jobs)
            -mappedBy = "assignedContractor" indicates that this is the inverse side of the relationship,
                 and the Job entity has an assignedContractor field that owns the relationship.
            -FetchType.LAZY: jobs are only loaded when accessed, not automatically with the contractor.

            NOTE:
            Without these annotations, you'd need to write hundreds of lines of SQL and JDBC code
            to handle database operations. Hibernate/JPA acts as the "translator" between Java
            objects and database tables.
    */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "contractor_service_areas",
            joinColumns = @JoinColumn(name = "contractor_id")
    )

    // Defines a set of service areas (zip codes) the contractor operates in
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

    //Enums declaration - creating a fixed set of specialty constants that represent a variety of contractor types.
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

    /**
     * Default constructor for JPA entity creation and framework usage.
     */
    public Contractor()
    {
        //calling parent constructor (User)
        super();
    }

    /**
     * Creates a new Contractor instance with required information.
     * Initializes rating to null and completed jobs count to 0.
     *
     * @param email contractor's email address
     * @param password contractor's password
     * @param firstName contractor's first name
     * @param lastName contractor's last name
     * @param phoneNumber contractor's phone number
     * @param companyName contractor's company name
     * @param description contractor's business description
     * @param address contractor's business address
     * @param licenseNumber contractor's license number
     * @param insured whether contractor is insured
     */
    public Contractor(String email, String password, String firstName, String lastName, String phoneNumber, String companyName,
                      String description, String address, String licenseNumber, boolean insured)
    {
        super(email, password, firstName, lastName, phoneNumber, UserRole.CONTRACTOR);
        this.companyName = companyName;
        this.description = description;
        this.address = address;
        this.licenseNumber = licenseNumber;
        this.insured = insured;
        this.rating = null;
        this.completedJobsCount = 0;
    }

    // Basic getters & setters
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

    /**
     * Returns all zip codes where this contractor provides services.
     *
     * @return Set of service area zip codes
     */
    public Set<String> getServiceAreas()
    {
        return serviceAreas;
    }

    public void setServiceAreas(Set<String> serviceAreas)
    {
        this.serviceAreas = serviceAreas;
    }

    /**
     * Returns all specialties this contractor offers.
     *
     * @return Set of contractor specialties
     */
    public Set<Specialty> getSpecialties()
    {
        return specialties;
    }

    public void setSpecialties(Set<Specialty> specialties)
    {
        this.specialties = specialties;
    }

    /**
     * Returns all jobs currently assigned to this contractor.
     *
     * @return List of assigned jobs
     */
    public List<Job> getAssignedJobs()
    {
        return assignedJobs;
    }

    public void setAssignedJobs(List<Job> assignedJobs)
    {
        this.assignedJobs = assignedJobs;
    }

    // Returns the list of all Review objects associated with this instance.
    public List<Review> getReviews()
    {
        return reviews;
    }

    public void setReviews(List<Review> reviews)
    {
        this.reviews = reviews;
    }

    /**
     * Assigns a job to this contractor and establishes bidirectional relationship.
     * Updates the contractor's job list and the job's assigned contractor.
     *
     * @param job to assign to this contractor
     */
    public void addJob(Job job)
    {
        assignedJobs.add(job);
        job.setAssignedContractor(this);
    }

    /**
     * Removes a job assignment from this contractor and clears bidirectional relationship.
     * Updates the contractor's job list and sets job's contractor to null.
     *
     * @param job the job to remove from this contractor
     */
    public void removeJob(Job job)
    {
        assignedJobs.remove(job);
        job.setAssignedContractor(null);
    }

    /**
     * Adds a customer review to this contractor and establishes bidirectional relationship.
     *
     * @param review the review to add to this contractor
     */
    public void addReview(Review review)
    {
        reviews.add(review);
        review.setContractor(this);
    }

    /**
     * Adds a new specialty to this contractor's skill set.
     *
     * @param specialty the specialty to add
     */
    public void addSpecialty(Specialty specialty)
    {
        this.specialties.add(specialty);
    }

    /**
     * Removes a specialty from this contractor's skill set.
     *
     * @param specialty the specialty to remove
     */
    public void removeSpecialty(Specialty specialty)
    {
        this.specialties.remove(specialty);
    }

    /**
     * Adds a new service area (zip code) to this contractor's coverage.
     *
     * @param serviceArea the zip code to add to service areas
     */
    public void addServiceArea(String serviceArea)
    {
        this.serviceAreas.add(serviceArea);
    }

    /**
     * Increments the completed jobs counter by 1.
     * Handles null case by initializing to 0 first.
     */
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

    /**
     * Calculates and updates the contractor's average rating based on all customer reviews.
     * Sets rating to null if no reviews exist.
     *
     * @return void - updates the rating field directly
     */

    public void updateRatingFromReviews()
    {
        if (reviews.isEmpty()) {
            this.rating = null;
            return;
        }

        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        double averageRating = sum / reviews.size();
        this.rating = averageRating;
    }

    /**
     * Checks if this contractor services a specific zip code area.
     *
     * @param zipCode the zip code to check
     * @return true if contractor services this area, false otherwise
     */
    public boolean servicesArea(String zipCode)
    {
        return serviceAreas.contains(zipCode);
    }

    /**
     * Checks if this contractor has a specific specialty.
     *
     * @param specialty the specialty to check for
     * @return true if contractor has this specialty, false otherwise
     */

    public boolean hasSpecialty(Specialty specialty)
    {
        return specialties.contains(specialty);
    }

    /**
     * Returns the contractor's primary specialty (first one in the set).
     * Returns null if no specialties are defined.
     *
     * @return primary specialty or null if none exist
     */
    public Specialty getPrimarySpecialty()
    {
        if (specialties.isEmpty())
        {
            return null;
        }
        else
        {
            //iterator().next() gets the first available element in whatever order the Set stores them
            return specialties.iterator().next();
        }
    }

    @Override
    public String toString() {
        return "Contractor {\n" +
                "  companyName: " + companyName + "\n" +
                "  description: " + description + "\n" +
                "  address: " + address + "\n" +
                "  licenseNumber: " + licenseNumber + "\n" +
                "  insured: " + insured + "\n" +
                "  rating: " + rating + "\n" +
                "  completedJobsCount: " + completedJobsCount + "\n" +
                "  serviceAreas: " + serviceAreas + "\n" +
                "  specialties: " + specialties + "\n" +
                // Ternary operator: if assignedJobs is not null, get size otherwise use 0
                "  assignedJobsCount: " + (assignedJobs != null ? assignedJobs.size() : 0) + "\n" +
                // Ternary operator: if reviews is not null, get size otherwise use 0
                "  reviewsCount: " + (reviews != null ? reviews.size() : 0) + "\n" +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        // Check if same reference
        if (this == o) return true;

        // Check if null or different class
        if (o == null || getClass() != o.getClass()) return false;

        // Cast to Contractor and compare emails
        Contractor contractor = (Contractor) o;
        return Objects.equals(getEmail(), contractor.getEmail());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getEmail());
    }

}
