package com.iremodelapi.web.controller;

import com.iremodelapi.domain.User;
import com.iremodelapi.security.JwtService;
import com.iremodelapi.service.UserService;
import com.iremodelapi.web.dto.AuthResponseDTO;
import com.iremodelapi.web.dto.LoginDTO;
import com.iremodelapi.web.dto.RegisterDTO;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST Controller for user authentication operations (register and login).
 * Handles HTTP requests for user registration and authentication, integrating with
 * Spring Security and JWT token generation for stateless API authentication.
 *
 * IMPORTANT ACKNOWLEDGMENT:
 * I leaned a tiny bit on AI assistance and Spring documentation for implementing
 * this controller class and its patterns. I had limited prior knowledge of:
 * - Spring MVC controller patterns and annotations
 * - RESTful API design and HTTP response handling
 * - Integration between web layer and security infrastructure
 * - DTO validation and request/response mapping
 *
 * This implementation represents learning from external resources to understand
 * enterprise web controller patterns and Spring MVC framework usage.
 *
 * @author Tyson Ringelstetter - with significant learning resources
 * @date 05/29/2025
 */
/*
    SPRING MVC WEB LAYER FEATURES (Beyond Basic Java):

    @RestController: Combines @Controller + @ResponseBody annotations
    - Marks this class as a Spring MVC controller
    - Automatically serializes return values to JSON
    - Handles HTTP request/response lifecycle

    @RequestMapping("/auth"): Base URL mapping for all endpoints in this controller
    - All methods inherit this base path: /auth/register, /auth/login
    - Groups related endpoints under common URL prefix

    @PostMapping: Maps HTTP POST requests to specific methods
    - RESTful convention: POST for creating resources such as registration & authentication
    - Handles request body parsing and response serialization

    ResponseEntity<T>: Spring's HTTP response wrapper
    - Allows control over HTTP status codes (200, 201, 400, 401)
    - Type-safe response body handling
    - Professional API response structure

    @Valid: Bean validation integration
    - Automatically validates request DTOs using validation annotations
    - Returns 400 Bad Request if validation fails
    - Integrates with Spring's validation framework

    This represents enterprise web API patterns that are genuinely complex
    and benefit from external learning resources and documentation.
*/
@RestController
@RequestMapping("/auth")
public class AuthController
{

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for dependency injection of required services.
     *
     * @param userService Service for user business logic operations
     * @param jwtService Service for JWT token generation and validation
     * @param authenticationManager Spring Security authentication coordinator
     * @param userDetailsService Service for loading user details for authentication
     */
    public AuthController(UserService userService, JwtService jwtService,
                          AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService)
    {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Handles user registration requests.
     * Creates new user account and immediately provides authentication token.
     *
     * HTTP ENDPOINT: POST /auth/register
     * REQUEST BODY: RegisterDTO with user information
     * RESPONSE: AuthResponseDTO with JWT token and user details
     *
     * @param registerDTO User registration data (validated automatically)
     * @return ResponseEntity with authentication token or error message
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterDTO registerDTO)
    {
        try {
            // Register the user (creates Contractor or Homeowner)
            User user = userService.registerUser(registerDTO);

            // Load UserDetails for JWT generation
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

            // Generate JWT token using custom JWT service
            String token = jwtService.generateToken(userDetails);

            AuthResponseDTO response = new AuthResponseDTO(
                    token,
                    user.getEmail(),
                    user.getRole().name()
            );

            // Return 201 CREATED with authentication response
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }
        catch (RuntimeException e)
        {
            return ResponseEntity.badRequest().body(
                    new AuthResponseDTO(null, null, "ERROR: " + e.getMessage()));
        }
    }

    /**
     * Handles user login/authentication requests.
     * Validates credentials and provides JWT token for authenticated access.
     *
     * HTTP ENDPOINT: POST /auth/login
     * REQUEST BODY: LoginDTO with email and password
     * RESPONSE: AuthResponseDTO with JWT token and user details
     *
     * @param loginDTO User login credentials (validated automatically)
     * @return ResponseEntity with authentication token or error message
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO)
    {
        try {
            // Authenticate the user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            // Get UserDetails from authentication
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Get user for role info
            User user = userService.findByEmail(loginDTO.getEmail());

            // Generate JWT token
            String token = jwtService.generateToken(userDetails);

            AuthResponseDTO response = new AuthResponseDTO(
                    token,                    // JWT token for API access
                    user.getEmail(),         // User identifier
                    user.getRole().name()    // User role for frontend routing
            );

            // Return 200 OK with authentication response
            return ResponseEntity.ok(response);

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthResponseDTO(null, null, "ERROR! Authentication failed")
            );
        }
    }
}