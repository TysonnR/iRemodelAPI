package com.iremodelapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for user registration requests.
 * Represents comprehensive user registration data with sophisticated validation rules.
 * Supports both Homeowner and Contractor registration with role-specific conditional fields.
 *
 * API CONTRACT:
 * Defines the request format for POST /auth/register endpoint.
 * Supports unified registration endpoint for both user types with conditional processing.
 *
 * REGEX PATTERN ACKNOWLEDGMENT:
 *  I used Claude.ai assistance to create and validate the regex patterns for:
 *  -Phone number validation (international format support)
 *  -ZIP code validation (5-digit and ZIP+4 formats)
 *  - Role validation (exact enum matching)
 *  * These regex patterns represent advanced validation requirements beyond basic
 *  * string validation
 *
 * @author Tyson Ringelstetter
 * @date 05/28/2025
 */
/*
    COMPLEX INPUT VALIDATION DTO PATTERN - ADVANCED ENTERPRISE VALIDATION:

    This DTO demonstrates the most sophisticated validation patterns in the application:
    - Multiple validation constraint types working together
    - Advanced regex patterns for business-specific data formats
    - Conditional field logic (contractor-specific fields)
    - Integration with complex business logic (factory pattern)

    Key Characteristics:
    - Comprehensive field validation covering all business requirements
    - Role-based conditional processing (single endpoint, multiple user types)
    - Advanced regex patterns for real-world data validation
    - Security-conscious design with proper input sanitization
    - Framework integration (Bean Validation + UserService factory pattern)

    API CONTRACT:
     Defines the request format for POST /auth/register endpoint.
     Supports unified registration endpoint for both user types with conditional processing.
*/
public class RegisterDTO
{
    /**
     * User's first name with length validation.
     * VALIDATION RULES:
     * - @NotBlank: Cannot be null, empty, or whitespace-only
     * - @Size(min=2, max=50): Enforces reasonable name length limits
     */
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    /**
     * User's last name with length validation.
     * VALIDATION RULES:
     * - @NotBlank: Cannot be null, empty, or whitespace-only
     * - @Size(min=2, max=50): Enforces reasonable name length limits
     */
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    /**
     * User's email address serving as unique account identifier.
     * VALIDATION RULES:
     * - @NotBlank: Cannot be null, empty, or whitespace-only
     * - @Email: Must conform to valid email format standards
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    /**
     * User's password for account security.
     * VALIDATION RULES:
     * - @NotBlank: Cannot be null, empty, or whitespace-only
     * - @Size(min=8): Minimum security requirement for password strength
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    /**
     * User's phone number with international format support.
     * Supports formats like: +1234567890, 1234567890
     * Regex pattern created with Claude.ai assistance.
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Please provide a valid phone number")
    private String phoneNumber;

    /**
     * User's role in the system determining account type and permissions.
     * Supports values: HOMEOWNER, CONTRACTOR
     * Regex pattern created with Claude.ai assistance for exact enum matching.
     *
     * Determines UserService.createContractor() vs createHomeowner() on the controller layer
     */
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(HOMEOWNER|CONTRACTOR)$", message = "Role must be either HOMEOWNER or CONTRACTOR")
    private String role;

    /**
     * User's location ZIP code with comprehensive format support.
     * Supports formats like: 12345, 12345-6789
     * Regex pattern created with Claude.ai assistance.
     */
    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Please provide a valid ZIP code")
    private String zipCode;

    /**
     * Contractor's company or business name.
     *
     * VALIDATION RULES:
     * - @Size(max=120): Prevents excessive length for database compatibility
     * - No @NotBlank: Field is optional and only relevant for contractor
     */
    @Size(max = 120, message = "Company name cannot exceed 120 characters")
    private String companyName;

    /**
     * Contractor's primary area of expertise (conditional field).
     *
     * VALIDATION RULES:
     * - @Size(max=50): Prevents excessive length for database compatibility
     * - No @NotBlank: Field is optional and only relevant for contractors
     */
    @Size(max = 50, message = "Specialty cannot exceed 50 characters")
    private String primarySpecialty;

    public RegisterDTO()
    {
        // Default constructor
    }

    /**
     * Parameterized constructor for creating registration requests programmatically.
     * Useful for testing and comprehensive user registration setup.
     *
     * @param firstName User's first name
     * @param lastName User's last name
     * @param email User's email address (unique account identifier)
     * @param password User's password for authentication
     * @param phoneNumber User's contact phone number
     * @param role User's system role (HOMEOWNER or CONTRACTOR)
     * @param zipCode User's location ZIP code
     * @param companyName Contractor's company name (null for homeowners)
     * @param primarySpecialty Contractor's primary expertise (null for homeowners)
     */
    public RegisterDTO(String firstName, String lastName, String email, String password,
                        String phoneNumber, String role, String zipCode, String companyName,
                        String primarySpecialty)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.zipCode = zipCode;
        this.companyName = companyName;
        this.primarySpecialty = primarySpecialty;
    }

    // Getters and Setters
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

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
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

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getPrimarySpecialty()
    {
        return primarySpecialty;
    }

    public void setPrimarySpecialty(String primarySpecialty)
    {
        this.primarySpecialty = primarySpecialty;
    }

    @Override
    public String toString()
    {
        return "RegisterDTO {\n" +
                "  firstName: '" + firstName + "'\n" +
                "  lastName: '" + lastName + "'\n" +
                "  email: '" + email + "'\n" +
                "  password: '[PROTECTED]'\n" +
                "  phoneNumber: '" + phoneNumber + "'\n" +
                "  role: '" + role + "'\n" +
                "  zipCode: '" + zipCode + "'\n" +
                "  companyName: '" + companyName + "'\n" +
                "  primarySpecialty: '" + primarySpecialty + "'\n" +
                "}";
    }

}
