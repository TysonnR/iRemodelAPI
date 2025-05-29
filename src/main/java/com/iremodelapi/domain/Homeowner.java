package com.iremodelapi.domain;

import jakarta.persistence.*;

import java.util.*;

/**
 * Represents a homeowner in the iRemodel platform.
 * Extends User to inherit basic user information and adds homeowner-specific attributes.
 * Manages property addresses, job postings, and reviews written by the homeowner.
 *
 * @author Tyson Ringelstetter
 * @date 5/27/2025
 */
//Marks this class as a JPA entity(java class that maps to a DB table using Java Persistence API (JPA))
@Entity
//Specifies the table name in the database
@Table(name = "homeowners")
/*
    Defines primary key column for inheritance mapping when using the
    JOINED table strategy. Tells JPA that homeowners table shares it primary key with the
    users table by the user_id foreign key
 */
@PrimaryKeyJoinColumn(name = "user_id")
public class Homeowner extends User
{
    //homeowner specific attributes
    //@Column maps the attribute to a database column, name is the column name
    @Column(name = "preferred_contact_method")
    private String preferredContactMethod = "email";

    //defines the mailing address column, can be null if not provided
    @Column(name = "mailing_address")
    private String mailingAddress;

    /*
            Collection Mappings - HIBERNATE/JPA FEATURES (Beyond Basic Java):

             *** HIBERNATE/JPA ANNOTATIONS (Database ORM Magic):
                -@ElementCollection: Maps a collection of basic types (property addresses) to a separate table
                -@CollectionTable: Defines the join table for storing the collection elements
                -@JoinColumn: Specifies the foreign key column that links to the Homeowner entity
                -@OneToMany: Defines a one-to-many relationship (One homeowner can have many jobs/reviews)
                -mappedBy = "homeowner" indicates that this is the inverse side of the relationship,
                     and the Job/Review entity has a homeowner field that owns the relationship.
                -FetchType.LAZY: collections are only loaded when accessed, not automatically with the homeowner.

                NOTE:
                Without these annotations, you'd need to write hundreds of lines of SQL and JDBC code
                to handle database operations. Hibernate/JPA acts as a translator between Java
                objects and database tables.
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "homeowner_properties",
            joinColumns = @JoinColumn(name = "homeowner_id")
    )
    //Defines a set of property addresses owned by this homeowner
    @Column(name = "property_address")
    private Set<String> propertyAddresses = new HashSet<>();

    // Relationship to jobs posted by this homeowner
    @OneToMany(mappedBy = "homeowner", fetch = FetchType.LAZY)
    private List<Job> postedJobs = new ArrayList<>();

    // Relationships to reviews written by this homeowner
    @OneToMany(mappedBy = "homeowner", fetch = FetchType.LAZY)
    private List<Review> writtenReviews = new ArrayList<>();

    /**
     * Default constructor for JPA entity creation and framework usage.
     */
    public Homeowner()
    {
        // Default constructor
    }

    /**
     * Creates a new Homeowner instance with required information.
     * Sets preferred contact method and mailing address for communication.
     *
     * @param email homeowner's email address
     * @param password homeowner's password
     * @param firstName homeowner's first name
     * @param lastName homeowner's last name
     * @param phoneNumber homeowner's phone number
     * @param preferredContactMethod how homeowner prefers to be contacted
     * @param mailingAddress homeowner's mailing address
     */
    public Homeowner(String email, String password, String firstName, String lastName, String phoneNumber,
                     String preferredContactMethod, String mailingAddress)
    {
        super(email, password, firstName, lastName, phoneNumber, UserRole.HOMEOWNER);
        this.preferredContactMethod = preferredContactMethod;
        this.mailingAddress = mailingAddress;
    }

    // Getters and Setters
    public String getPreferredContactMethod()
    {
        return preferredContactMethod;
    }

    public void setPreferredContactMethod(String preferredContactMethod)
    {
        this.preferredContactMethod = preferredContactMethod;
    }

    public String getMailingAddress()
    {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress)
    {
        this.mailingAddress = mailingAddress;
    }

    /**
     * Returns all property addresses owned by this homeowner.
     *
     * @return Set of property addresses
     */
    public Set<String> getPropertyAddresses()
    {
        return propertyAddresses;
    }

    public void setPropertyAddresses(Set<String> propertyAddresses)
    {
        this.propertyAddresses = propertyAddresses;
    }

    /**
     * Returns all jobs posted by this homeowner.
     *
     * @return List of posted jobs
     */
    public List<Job> getPostedJobs()
    {
        return postedJobs;
    }

    public void setPostedJobs(List<Job> postedJobs)
    {
        this.postedJobs = postedJobs;
    }

    /**
     * Returns all reviews written by this homeowner.
     *
     * @return List of written reviews
     */
    public List<Review> getWrittenReviews()
    {
        return writtenReviews;
    }

    public void setWrittenReviews(List<Review> writtenReviews)
    {
        this.writtenReviews = writtenReviews;
    }

    /**
     * Helper Methods - Adds a new property address to this homeowner's property list.
     *
     * @param address the property address to add
     */
    public void addPropertyAddress(String address)
    {
        this.propertyAddresses.add(address);
    }

    /**
     * Removes a property address from this homeowner's property list.
     *
     * @param address the property address to remove
     */
    public void removePropertyAddress(String address)
    {
        this.propertyAddresses.remove(address);
    }

    /**
     * Posts a new job and establishes bidirectional relationship.
     * Updates both the homeowner's job list and the job's homeowner reference.
     *
     * @param job the job to post
     */
    public void addPostedJob(Job job)
    {
        postedJobs.add(job);
        // Maintain bidirectional relationship
        job.setHomeowner(this);
    }

    /**
     * Removes a posted job and clears bidirectional relationship.
     * Updates both the homeowner's job list and sets job's homeowner to null.
     *
     * @param job the job to remove
     */
    public void removePostedJob(Job job)
    {
        postedJobs.remove(job);
        // Clean up bidirectional relationship
        job.setHomeowner(null);
    }

    /**
     * Adds a review written by this homeowner and establishes bidirectional relationship.
     *
     * @param review the review to add
     */
    public void addReview(Review review)
    {
        writtenReviews.add(review);
        // maintain bidirectional relationship
        review.setHomeowner(this);
    }

    /**
     * Removes a review written by this homeowner and clears bidirectional relationship.
     *
     * @param review the review to remove
     */
    public void removeReview(Review review)
    {
        writtenReviews.remove(review);
        // Clean up bidirectional relationship
        review.setHomeowner(null);
    }

    @Override
    public String toString()
    {
        return "Homeowner:\n" +
                "  id: " + getId() + "\n" +
                "  email: " + getEmail() + "\n" +
                "  firstName: " + getFirstName() + "\n" +
                "  lastName: " + getLastName() + "\n" +
                "  phoneNumber: " + getPhoneNumber() + "\n" +
                "  preferredContactMethod: " + preferredContactMethod + "\n" +
                "  mailingAddress: " + mailingAddress + "\n" +
                "  propertyAddresses: " + propertyAddresses + "\n" +
                "  postedJobsCount: " + postedJobs.size() + "\n" +
                "  writtenReviewsCount: " + writtenReviews.size();
    }

    @Override
    public boolean equals(Object o)
    {
        // Check if same reference
        if (this == o) return true;

        // Check if null or different class
        if (o == null || getClass() != o.getClass()) return false;

        // Cast to Homeowner and compare emails
        Homeowner homeowner = (Homeowner) o;
        return Objects.equals(getEmail(), homeowner.getEmail());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getEmail());
    }

}
