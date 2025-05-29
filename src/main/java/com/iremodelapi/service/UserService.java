package com.iremodelapi.service;

import com.iremodelapi.domain.*;
import com.iremodelapi.repository.*;
import com.iremodelapi.web.dto.RegisterDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for user registration and management operations.
 * Handles creation of different user types (Contractor/Homeowner) and user authentication setup.
 * Integrates with Spring Security for password encoding and Spring transactions for data consistency.
 *
 * This service bridges the gap between API layer (DTOs) and domain layer (entities)
 * while coordinating with security and transaction management systems.
 *
 * @author Tyson Ringelstetter
 * @date 05/28/2025
 */
/*
    SPRING FEATURES:

    @Service: Marks this as a Spring service component for dependency injection

    @Transactional: Spring transaction management annotation
    - Ensures all database operations in service methods happen within a transaction
    - Automatically commits successful operations or rolls back on exceptions
    - Provides ACID properties (Atomicity, Consistency, Isolation, Durability)
    - Applied at class level = all public methods are transactional

    PasswordEncoder: Integration with Spring Security framework
    - Uses BCrypt encoder configured in SecurityConfig
    - Provides secure password hashing for user authentication
    - Connects service layer with security infrastructure
*/
@Service
@Transactional
public class UserService
{

    /*
        SPRING DEPENDENCY INJECTION:
        UserRepository: Database operations for user entities
        PasswordEncoder: Spring Security component for password hashing
    */
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for dependency injection of required components.
     *
     * @param userRepository Repository for user database operations
     * @param passwordEncoder Spring Security password encoder for secure hashing
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system based on their specified role.
     * Implements statements to create appropriate user subtype (Contractor or Homeowner).
     *
     * @param registerDTO Data transfer object containing user registration information
     * @return Saved user entity (Contractor or Homeowner subtype)
     * @throws RuntimeException if email already exists or invalid role provided
     */
    public User registerUser(RegisterDTO registerDTO)
    {
        // Check if user already exists
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent())
        {
            throw new RuntimeException("User with email " + registerDTO.getEmail() + " already exists");
        }

        User user;

        /*
            Based on role ENUM string, method executes by its corresponding role
            - createContractor() for contractor-specific initialization
            - createHomeowner() for homeowner-specific initialization
        */
        if ("CONTRACTOR".equals(registerDTO.getRole()))
        {
            user = createContractor(registerDTO);
        }
        else if ("HOMEOWNER".equals(registerDTO.getRole()))
        {
            user = createHomeowner(registerDTO);
        }
        else
        {
            throw new RuntimeException("Invalid role: " + registerDTO.getRole());
        }

        return userRepository.save(user);
    }

    /**
     * Finds a user by their email address.
     * Used for authentication and user lookup operations.
     *
     * @param email The email address to search for
     * @return User entity with the specified email
     * @throws RuntimeException if no user found with the given email
     */
    public User findByEmail(String email)
    {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    /**
     * Method for creating Contractor entities from registration data.
     * Handles contractor-specific initialization including specialties and their service areas.

     * @param registerDTO Registration data containing contractor information
     * @return Initialized Contractor entity ready for database persistence
     */
    private Contractor createContractor(RegisterDTO registerDTO)
    {
        Contractor contractor = new Contractor();

        // Common User Field Initialization: All user types share basic User entity fields
        contractor.setEmail(registerDTO.getEmail());
        // Password encoding using Spring Security BCrypt encoder
        contractor.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        contractor.setFirstName(registerDTO.getFirstName());
        contractor.setLastName(registerDTO.getLastName());
        contractor.setPhoneNumber(registerDTO.getPhoneNumber());
        contractor.setRole(User.UserRole.CONTRACTOR);
        contractor.setZipCode(registerDTO.getZipCode());

        // Set Contractor-specific fields
        if (registerDTO.getCompanyName() != null)
        {
            contractor.setCompanyName(registerDTO.getCompanyName());
        }
        else
        {
            contractor.setCompanyName("");
        }

        contractor.setRating(null); // New contractor starts with no rating
        contractor.setCompletedJobsCount(0);

        // Add service area (their zip code)
        contractor.addServiceArea(registerDTO.getZipCode());

        // Add specialty if provided
        if (registerDTO.getPrimarySpecialty() != null && !registerDTO.getPrimarySpecialty().isEmpty())
        {
            try
            {
                Contractor.Specialty specialty = Contractor.Specialty.valueOf(
                        registerDTO.getPrimarySpecialty().toUpperCase()
                );
                contractor.addSpecialty(specialty);
            }
            catch (IllegalArgumentException e) {
                // If specialty doesn't match enum, add GENERAL as default
                contractor.addSpecialty(Contractor.Specialty.GENERAL);
            }
        }

        return contractor;
    }

    /**
     * Factory method for creating Homeowner entities from registration data.
     * Handles homeowner-specific initialization including contact preferences.
     *
     * HOMEOWNER INITIALIZATION:
     * - Sets all common User fields (email, name, phone, etc.)
     * - Encodes password using Spring Security BCrypt encoder
     * - Initializes homeowner-specific defaults (preferred contact method)
     * - Simpler than contractor creation due to fewer specialized fields
     *
     * @param registerDTO Registration data containing homeowner information
     * @return Initialized Homeowner entity ready for database persistence
     */
    private Homeowner createHomeowner(RegisterDTO registerDTO)
    {
        Homeowner homeowner = new Homeowner();

        // Common User Field Initialization: All user types share basic User entity fields

        homeowner.setEmail(registerDTO.getEmail());
        homeowner.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        homeowner.setFirstName(registerDTO.getFirstName());
        homeowner.setLastName(registerDTO.getLastName());
        homeowner.setPhoneNumber(registerDTO.getPhoneNumber());
        homeowner.setRole(User.UserRole.HOMEOWNER);
        homeowner.setZipCode(registerDTO.getZipCode());

        // Set Homeowner-specific defaults
        homeowner.setPreferredContactMethod("email");

        return homeowner;
    }
}