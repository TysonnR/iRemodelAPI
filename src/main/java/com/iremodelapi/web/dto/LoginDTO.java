package com.iremodelapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for user authentication requests.
 * Represents the input data for user login operations with built-in validation rules.
 * Integrates with Spring Boot's Bean Validation framework for automatic input validation.
 *
 * VALIDATION INTEGRATION:
 * This DTO demonstrates enterprise input validation patterns using Jakarta Bean Validation:
 * 1. Email format validation using @Email annotation
 * 2. Required field validation using @NotBlank annotation
 * 3. Password strength validation using @Size annotation
 * 4. Automatic validation triggering via @Valid in controller methods
 * 5. Security-conscious toString implementation (password protection)
 *
 * AUTHENTICATION WORKFLOW:
 * Used in the authentication flow:
 * - Client sends JSON with email/password → LoginDTO
 * - Spring validates using annotations → Returns 400 if invalid
 * - AuthController processes → AuthenticationManager authenticates
 * - Success → JWT token generation and AuthResponseDTO return
 *
 * API CONTRACT:
 * Defines the request format for POST /auth/login endpoint.
 * Ensures consistent validation rules and error messages across the application.
 *
 * @author Tyson Ringelstetter
 * @date 05/28/2025
 */
/*
    INPUT VALIDATION DTO PATTERN - SECURITY INTEGRATION:

    This DTO demonstrates input validation beyond simple data transfer:
    - Security-conscious design (password protection in logging)
    - Client-friendly error messages for validation failures
    - Integration with Spring Security authentication

    Key Characteristics:
    - Declarative validation using annotations (no manual validation code)
    - Consistent error message format across API endpoints
    - Security best practices (password masking in string representation)
    - Framework integration (Spring Boot validation, Spring Security)

*/
public class LoginDTO
{
    /**
     * User's email address serving as username for authentication.
     *
     * - @NotBlank: Cannot be null, empty, or contain only whitespace
     * - @Email: Must be valid email format (uses regex pattern validation)
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    /**
     * User's password for authentication.
     *
     * - @NotBlank: Cannot be null, empty, or contain only whitespace
     * - @Size(min = 8): Must be at least 8 characters for basic security
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    public LoginDTO()
    {
        // Default constructor
    }

    /**
     * Parameterized constructor for creating login requests programmatically.
     * Useful for testing and internal service-to-service authentication.
     *
     * @param email User's email address for authentication
     * @param password User's password for authentication
     */
    public LoginDTO(String email, String password)
    {
        this.email = email;
        this.password = password;
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

    @Override
    public String toString()
    {
        return "LoginDTO {\n" +
                "  email: '" + email + "'\n" +
                "  password: '[PROTECTED]'\n" +
                "}";
    }

}
