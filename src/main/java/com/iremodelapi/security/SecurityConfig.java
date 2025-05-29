package com.iremodelapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security Configuration for the iRemodel API.
 * Configures JWT-based stateless authentication, URL access rules, and security filters.
 * Provides separate configurations for development and production environments.
 *
 * IMPORTANT ACKNOWLEDGMENT:
 * Most of this code is NOT my original work. This implementation is based on:
 * - YouTube crash course videos on Spring Security configuration
 * - AI assistance to adapt security configuration for this specific project
 * - Spring Security documentation and community examples
 *
 * I had no prior knowledge of Spring Security configuration, HTTP security rules,
 * authentication providers, or enterprise security architecture before this project,
 * and insufficient time to learn these complex enterprise security concepts from scratch.
 * This represents advanced enterprise security implementation that is well beyond my basic Java
 * skills and Intro CS coursework but I was determined to not let this hold me back,
 * while also diving deep into some complex concepts!
 *
 * @author Tyson - with significant external learning resources (ProgrammingWithMosh on Youtube, Claude.ai)
 * @date 5/28/2025
 */
/*
    ENTERPRISE SECURITY CONFIGURATION FEATURES (Far Beyond Basic Java):

    @Configuration: Marks this as a Spring configuration class containing Bean definitions

    @EnableWebSecurity: Enables Spring Security's web security features
    - Activates Spring Security filter chain for HTTP requests
    - Enables method-level security annotations
    - Integrates with Spring MVC security

    @EnableMethodSecurity: Enables method-level security annotations
    - Allows @PreAuthorize, @PostAuthorize on methods
    - Enables role-based access control at method level

    SecurityFilterChain: Spring Security's modern configuration approach
    - Replaces older WebSecurityConfigurerAdapter pattern
    - Defines HTTP security rules using lambda expressions
    - Configures authentication, authorization, and filters

    This involves advanced concepts: web security architecture, HTTP request filtering,
    cryptographic authentication, stateless session management, and enterprise
    authorization patterns. This is professional-level security configuration.
*/
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig
{
    /*
        Dependency Injection - SPRING FRAMEWORK FEATURE:
        Security components injected by Spring's dependency injection container.
        Constructor injection ensures dependencies are available at creation time.
    */
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for dependency injection of security components.
     *
     * @param jwtAuthFilter Custom JWT filter for processing authentication tokens
     * @param userDetailsService Service for loading user details from database
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService)
    {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /*
        Profile-Based Configuration - SPRING PROFILES FEATURE:

        @Profile("dev") - Only active when "dev" profile is enabled
        @Profile("!dev") - Active when "dev" profile is NOT enabled (production)

        This allows different security configurations for different environments:
        - Development: Less restrictive, allows H2 console access
        - Production: More secure, blocks development-only features

        Profiles are set via: spring.profiles.active=dev in application properties
    */

    /**
     * Security configuration for development environment.
     * Less restrictive rules to allow development tools and debugging.
     * Only active when "dev" profile is enabled.
     *
     * DEVELOPMENT-SPECIFIC FEATURES:
     * - Allows H2 database console access
     * - Relaxed Content Security Policy for development tools
     * - Same authentication but more permissive access rules
     *
     * @param http HttpSecurity configuration object
     * @return Configured SecurityFilterChain for development
     * @throws Exception if configuration fails
     */
    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception
    {
        /*
            Spring Security Fluent Configuration API - MODERN SPRING SECURITY:
            Uses lambda expressions and method chaining for readable configuration.
            Each method configures a different aspect of security.
        */
        return http
                // Disable CSRF for stateless JWT authentication
                .csrf(AbstractHttpConfigurer::disable)

                // Configure URL access rules
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to authentication and static resources
                        .requestMatchers("/auth/**", "/public/**", "/error",
                                "/", "*.html", "*.css", "*.js", "/static/**").permitAll()
                        // Allow H2 console access in development (DEVELOPMENT ONLY!)
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Configure stateless session management for JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configure headers for H2 console frame embedding (DEVELOPMENT ONLY!)
                .headers(headers -> headers.contentSecurityPolicy(csp ->
                        csp.policyDirectives("frame-ancestors 'self'")
                ))

                // Set authentication provider for user validation
                .authenticationProvider(authenticationProvider())

                // Add JWT filter before Spring's default authentication filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Security configuration for production environment.
     * More restrictive rules for production security.
     * Active when "dev" profile is NOT enabled.
     *
     * PRODUCTION-SPECIFIC FEATURES:
     * - No H2 console access (security risk in production)
     * - Stricter Content Security Policy
     * - Same authentication with production-hardened access rules
     *
     * @param http HttpSecurity configuration object
     * @return Configured SecurityFilterChain for production
     * @throws Exception if configuration fails
     */
    @Bean
    @Profile("!dev")
    public SecurityFilterChain productionSecurityFilterChain(HttpSecurity http) throws Exception
    {
        return http
                // Disable CSRF for stateless JWT authentication
                .csrf(AbstractHttpConfigurer::disable)

                // Configure URL access rules (NO H2 console access!)
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to authentication and static resources
                        .requestMatchers("/auth/**", "/public/**", "/error",
                                "/", "*.html", "*.css", "*.js", "/static/**").permitAll()
                        // All other requests require authentication (no H2 console!)
                        .anyRequest().authenticated()
                )

                // Configure stateless session management for JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Set authentication provider for user validation
                .authenticationProvider(authenticationProvider())

                // Add JWT filter before Spring's default authentication filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /*
        Spring Bean Configuration - DEPENDENCY INJECTION SETUP:

        @Bean methods create Spring-managed objects that can be injected elsewhere.
        These beans configure the authentication infrastructure used throughout the app.
    */

    /**
     * Configures the authentication provider for validating user credentials.
     * Uses database-backed authentication with BCrypt password encoding.
     *
     * AUTHENTICATION FLOW:
     * 1. User provides username/password
     * 2. UserDetailsService loads user from database
     * 3. PasswordEncoder verifies password against stored hash
     * 4. Authentication succeeds or fails based on validation
     *
     * @return Configured DaoAuthenticationProvider for database authentication
     */
    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);  // How to load users
        authProvider.setPasswordEncoder(passwordEncoder());      // How to verify passwords
        return authProvider;
    }

    /**
     * Configures password encoding using BCrypt algorithm.
     * BCrypt is industry standard for secure password hashing.
     *
     * BCRYPT FEATURES:
     * - Adaptive hashing (configurable work factor)
     * - Built-in salt generation (prevents rainbow table attacks)
     * - Computationally expensive (prevents brute force attacks)
     * - Industry standard for password security
     *
     * @return BCryptPasswordEncoder for secure password hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication manager for handling authentication requests.
     * Used by Spring Security to coordinate authentication providers.
     *
     * @param config Spring's authentication configuration
     * @return Configured AuthenticationManager
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }
}