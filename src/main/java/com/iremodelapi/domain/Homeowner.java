package com.iremodelapi.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "homeowners")
@PrimaryKeyJoinColumn(name = "user_id")
public class Homeowner extends User
{
    //homeowner specific attributes
    @Column(name = "preferred_contact_method")
    private String preferredContactMethod = "email";

    @Column(name = "mailing_address")
    private String mailingAddress;

    //collection of property addresses
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "homeowner_properties",
            joinColumns = @JoinColumn(name = "homeowner_id")
    )
    @Column(name = "property_address")
    private Set<String> propertyAddresses = new HashSet<>();

    // Relationship to jobs posted by this homeowner
    @OneToMany(mappedBy = "homeowner", fetch = FetchType.LAZY)
    private List<Job> postedJobs = new ArrayList<>();

    // Relationships to reviews written by this homeowner
    @OneToMany(mappedBy = "homeowner", fetch = FetchType.LAZY)
    private List<Review> writtenReviews = new ArrayList<>();

    // Default constructor (required by JPA)
    public Homeowner()
    {
        // Default constructor
    }

    public Homeowner(String email, String password, String fullName, String phoneNumber,
                     String preferredContactMethod, String mailingAddress)
    {
        super(email, password, fullName, phoneNumber, UserRole.HOMEOWNER);
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

    public Set<String> getPropertyAddresses()
    {
        return propertyAddresses;
    }

    public void setPropertyAddresses(Set<String> propertyAddresses)
    {
        this.propertyAddresses = propertyAddresses;
    }

    public List<Job> getPostedJobs()
    {
        return postedJobs;
    }

    public void setPostedJobs(List<Job> postedJobs)
    {
        this.postedJobs = postedJobs;
    }

    public List<Review> getWrittenReviews()
    {
        return writtenReviews;
    }

    public void setWrittenReviews(List<Review> writtenReviews)
    {
        this.writtenReviews = writtenReviews;
    }

    // Helper methods for collections to add/remove addresses
    public void addPropertyAddress(String address)
    {
        this.propertyAddresses.add(address);
    }

    public void removePropertyAddress(String address)
    {
        this.propertyAddresses.remove(address);
    }

    // Helper methods for relationship management
    public void addPostedJob(Job job)
    {
        postedJobs.add(job);
        job.setHomeowner(this);
    }

    public void removePostedJob(Job job)
    {
        postedJobs.remove(job);
        job.setHomeowner(null);
    }

    public void addReview(Review review)
    {
        writtenReviews.add(review);
        review.setHomeowner(this);
    }

    public void removeReview(Review review)
    {
        writtenReviews.remove(review);
        review.setHomeowner(null);
    }

    @Override
    public String toString()
    {
        return "Homeowner{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", preferredContactMethod='" + preferredContactMethod + '\'' +
                ", mailingAddress='" + mailingAddress + '\'' +
                ", propertyAddresses=" + propertyAddresses +
                ", postedJobs=" + (postedJobs != null ? postedJobs.size() : 0) +
                ", writtenReviews=" + (writtenReviews != null ? writtenReviews.size() : 0) +
                '}';
    }
}
