package com.iremodelapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * JWT Authentication Filter for processing JWT tokens on incoming HTTP requests.
 * Extends Spring Security's OncePerRequestFilter to intercept requests and validate JWT tokens.
 * Sets up Spring Security context with authenticated user information for authorized requests.
 *
 * IMPORTANT ACKNOWLEDGMENT:
 * Most of this code is NOT my original work. This implementation is based on:
 * - YouTube crash course video on Spring Security + JWT authentication
 * - AI assistance to adapt the code for this specific project
 *
 * I had no prior knowledge of JWT authentication, Spring Security filters, or enterprise
 * security patterns before this project, and insufficient time to learn these concepts
 * from scratch. This represents advanced enterprise security implementation that is
 * well beyond my basic Java skills and Intro CS coursework but I was determined to not let this hold me back,
 * while also diving deep into some complex concepts!
 *
 * @author Tyson - with significant external learning resources (ProgrammingWithMosh on Youtube, Claude.ai)
 * @date 5/28/2025
 */
/*
    ENTERPRISE SECURITY FEATURES (Far Beyond Basic Java & my current knowledge):

    @Component: Marks this as a Spring-managed component for dependency injection

    OncePerRequestFilter: Spring Security base class that ensures the filter
    runs exactly once per HTTP request (even with request forwarding/redirects)

    JWT (JSON Web Token): Industry-standard authentication mechanism for stateless APIs
    - Contains encoded user information and expiration
    - Digitally signed to prevent tampering
    - Allows stateless authentication (no server-side sessions)

    Spring Security Context: Thread-local storage for current user's authentication state
    - SecurityContextHolder manages the current user's security context
    - Used throughout the application to check current user permissions

    This is VERY advanced - combines web security, cryptography, HTTP processing,
    and enterprise authentication patterns. This is professional-level security code.
*/
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    /*
        Dependency Injection - SPRING FRAMEWORK FEATURE:
        These services are injected by Spring's dependency injection container.
        Constructor injection is preferred over field injection for testability.
    */
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for dependency injection of required services.
     * Spring automatically provides these dependencies based on configuration.
     *
     * @param jwtService Service for JWT token operations (creation, validation, parsing)
     * @param userDetailsService Spring Security service for loading user information
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService)
    {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Main filter method that processes each HTTP request for JWT authentication.
     * This method runs for every HTTP request to check for valid JWT tokens.
     *
     * REQUEST PROCESSING FLOW:
     * 1. Extract Authorization header from HTTP request
     * 2. Validate header contains Bearer token
     * 3. Parse JWT token and extract username
     * 4. Load user details and validate token
     * 5. Set Spring Security authentication context
     * 6. Continue with request processing
     *
     * @param request HTTP request object containing headers and request data
     * @param response HTTP response object for sending response data
     * @param filterChain Chain of filters to continue request processing
     * @throws ServletException if servlet processing error occurs
     * @throws IOException if input/output error occurs
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Extract Authorization header from HTTP request
        // Standard format: "Authorization: Bearer <jwt-token>"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Check if Authorization header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No valid auth header - continue without authentication
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token by removing "Bearer " prefix (7 characters)
        // "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." -> "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        jwt = authHeader.substring(7);

        // Extract username (email) from JWT token payload
        // JWT tokens contain encoded user information that can be decoded
        userEmail = jwtService.extractUsername(jwt);

        // Process authentication if user email found and no existing authentication
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load full user details from database using the extracted email
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Validate the JWT token against the user details
            // Checks token signature, expiration, and user match
            if (jwtService.isTokenValid(jwt, userDetails)) {

                /*
                    Create Spring Security Authentication Token:
                    UsernamePasswordAuthenticationToken represents an authenticated user
                    - Principal: UserDetails object with user information
                    - Credentials: null (JWT already validated, no password needed)
                    - Authorities: User's roles/permissions from UserDetails
                */
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,        // Principal (the authenticated user)
                        null,              // Credentials (not needed for JWT)
                        userDetails.getAuthorities()  // Authorities (roles/permissions)
                );

                // Add additional request details to the authentication token
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Set the authentication in Spring Security context
                // This makes the user "logged in" for the duration of this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue with the request processing chain
        // Other filters and eventually the controller will process the request
        filterChain.doFilter(request, response);
    }
}