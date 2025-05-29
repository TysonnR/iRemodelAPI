package com.iremodelapi.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.*;

/**
 * Base user entity for the iRemodel platform.
 * Serves as the parent class for Contractor and Homeowner entities using JPA inheritance.
 * Contains common user attributes like contact information, authentication, and timestamps.
 *
 * @author Tyson Ringelstetter
 * @date 5/28/2025
 */
// marks this class as a JPA entity, will be mapped to a database table
@Entity
//Specifies the table name in the database
@Table(name = "users")
/*
    JPA Inheritance Strategy - HIBERNATE/JPA ENTERPRISE FEATURE:
    @Inheritance(strategy = InheritanceType.JOINED) creates a separate table for each class
    in the inheritance hierarchy. The "users" table stores common fields, while
    "contractors" and "homeowners" tables store specific fields and join to users with a foreign key.

    This is different from basic Java inheritance, as JPA maps the inheritance to database tables

    NOTE: I used some AI guidance with the inheritance annotation, GeneratedValue annotation,
    inheritance type and generation type since these are advanced JPA concepts. I mainly used guidance on what
    annotation to choose and for the Types for my specific application and the reasoning.
*/
@Inheritance(strategy = InheritanceType.JOINED)

public class User
{
    //@Id marks this field as the primary key for the entity
    @Id
    //@GeneratedValue with IDENTITY strategy lets database auto-generate the ID values
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
        Unique Constraint - Database Integrity Feature:
        unique = true creates a unique constraint in the database, ensuring no two users
        can have the same email address.
    */
    @Column(nullable = false, unique = true)
    private String email;

    //Password field
    @Column(nullable = false)
    private String password;

    //User's first name
    @Column(nullable = false)
    private String firstName;

    //User's last name
    @Column(nullable = false)
    private String lastName;

    //User's phone number for contact
    @Column(nullable = false)
    private String phoneNumber;

    /*
        Enum Storage - HIBERNATE/JPA FEATURE:
        @Enumerated(EnumType.STRING) stores enum values as strings in the database
        instead of numbers. This is safer because adding new enum values
        won't break existing data.
    */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /*
       Optional Location Field:
       ZIP code for user's location, supports both 5-digit (12345) and ZIP+4 (12345-6789) formats.
       length = 10 sets the maximum column length in the database.
   */
    @Column(name = "zip_code", length = 10)
    private String zipCode;

    /*
        Timestamp Management - JAVA TIME API: updatable = false
        prevents the createdAt timestamp from being modified after initial creation.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Default constructor for JPA entity creation and framework usage.
     */
    public User()
    {
        // Default constructor
    }

    /**
     * Creates a new User instance with required information.
     * Timestamps will be set automatically by JPA lifecycle methods.
     *
     * @param email user's email address (must be unique)
     * @param password user's password (should be hashed in production)
     * @param firstName user's first name
     * @param lastName user's last name
     * @param phoneNumber user's phone number
     * @param role user's role in the system (HOMEOWNER, CONTRACTOR, ADMIN)
     */
    public User(String email, String password, String firstName, String lastName, String phoneNumber, UserRole role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        // createdAt and updatedAt will be set by @PrePersist
    }

    // Getters and Setters for each attribute
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public UserRole getRole()
    {
        return role;
    }

    public void setRole(UserRole role)
    {
        this.role = role;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
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

    /*
        JPA Lifecycle Methods - HIBERNATE/JPA FEATURES (Beyond Basic Java):
        These methods are automatically called by JPA at specific times
    */

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
     * JPA lifecycle method - automatically called before entity is updated in database.
     * Updates the updatedAt timestamp to current time.
     */
    @PreUpdate
    protected void onUpdate()
    {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString()
    {
        return "User:\n" +
                "  id: " + id + "\n" +
                "  email: " + email + "\n" +
                "  firstName: " + firstName + "\n" +
                "  lastName: " + lastName + "\n" +
                "  phoneNumber: " + phoneNumber + "\n" +
                "  role: " + role + "\n" +
                "  zipCode: " + zipCode + "\n" +
                "  createdAt: " + createdAt + "\n" +
                "  updatedAt: " + updatedAt;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return email.equals(user.email);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(email);
    }

    /**
     * Enum defining the different types of users
     * Used to control access permissions and determine user-specific functionality.
     */
    public enum UserRole
    {
        HOMEOWNER,
        CONTRACTOR,
        ADMIN
    }

}
