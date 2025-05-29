package com.iremodelapi.web.dto;

/**
 * Data Transfer Object (DTO) for authentication response data.
 * Defines the API contract for authentication endpoints (register and login).
 * Contains user authentication information returned to clients after successful authentication.
 *
 * DTO PATTERN PURPOSE:
 * DTOs serve as the API contract layer, separating internal domain models from
 * external API representations. This class defines exactly what authentication
 * data is exposed to API clients while hiding internal implementation details.
 *
 * API CONTRACT:
 * This DTO represents the standardized response format for:
 * - POST /auth/register (user registration)
 * - POST /auth/login (user authentication)
 *
 * JSON SERIALIZATION:
 * Spring automatically converts this object to JSON for HTTP responses
 * and from JSON for HTTP requests using Jackson serialization.
 *
 * @author Tyson Ringelstetter
 * @date 05/27/2025
 */
/*
    DATA TRANSFER OBJECT (DTO) PATTERN - ENTERPRISE API DESIGN:

    DTOs are simple objects designed for data transport between layers:
    - Web Layer to and from External Clients (JSON over HTTP)
    - Service Layer to and from Web Layer (internal data transfer)
    - Separate API contracts from domain models

    Key Characteristics:
    - No business logic (pure data containers)
    - Focused on specific API operations
    - Control over what data is exposed externally
    - Optimized for serialization/deserialization

    This represents standard enterprise pattern for API design,
*/
public class AuthResponseDTO
{
    /*
        Authentication Response Data Fields:
        These fields represent information needed by clients after successful authentication to use the API
        and determine user context.
    */
    // JWT authentication token to access the API
    private String token;

    //User's email address to provide user id info
    private String email;

    //User's role in the system (Contractor, Homeowner) & determines what features/pages user can access in the
    // frontend
    private String role;

    public AuthResponseDTO()
    {
        // Default constructor
    }

    /**
     * Parameterized constructor for creating populated authentication responses.
     * Used by controllers to create response objects with authentication data.
     *
     * @param token JWT authentication token for API access
     * @param email User's email address/username
     * @param role User's system role (CONTRACTOR/HOMEOWNER/ADMIN)
     */
    public AuthResponseDTO(String token, String email, String role)
    {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getEmail()
    {
        return email;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }
    //Overridden toString method
    @Override
    public String toString() {
        return "AuthResponseDTO {\n" +
                "  token: '" + token + "'\n" +
                "  email: '" + email + "'\n" +
                "  role: '" + role + "'\n" +
                "}";
    }

}
