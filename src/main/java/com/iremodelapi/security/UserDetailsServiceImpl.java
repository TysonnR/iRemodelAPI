
package com.iremodelapi.security;

import com.iremodelapi.domain.User;
import com.iremodelapi.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

/**
 * Custom implementation of Spring Security's UserDetailsService interface.
 * Acts as a bridge between Spring Security's authentication system and the custom User entity.
 * Loads user information from the database and converts it to Spring Security's UserDetails format.
 *
 * IMPORTANT ACKNOWLEDGMENT:
 * Most of this code is NOT my original work. This implementation is based on:
 * - YouTube crash course videos on Spring Security UserDetailsService implementation
 * - AI assistance to adapt the user loading logic for this specific project
 * - Spring Security documentation on UserDetailsService interface
 *
 * I had no prior knowledge of Spring Security's UserDetailsService interface, authority mapping,
 * or the integration between custom entities and Spring Security's authentication system
 * before this project, and insufficient time to learn these complex enterprise security
 * integration patterns from scratch.
 *
 * @author Tyson - with significant external learning resources (ProgrammingWithMosh on Youtube, Claude.ai,
 *                  Spring Security Documentation)
 * @date 5/28/2025
 */
/*
    SPRING SECURITY INTEGRATION FEATURES (Far Beyond Basic Java):

    @Service: Marks this as a Spring service component for dependency injection

    UserDetailsService: Spring Security interface for loading user-specific data
    - Core interface in Spring Security authentication process
    - loadUserByUsername() method is called during authentication
    - Returns UserDetails object that Spring Security uses for authentication

    UserDetails vs User Entity:
    - User: Our custom domain entity from the database
    - UserDetails: Spring Security's interface for authentication data
    - This class converts between the two (domain entity -> security object)

    Authority/Role Mapping:
    - Spring Security requires "ROLE_" prefix for role-based security
    - Converts UserRole enum (CONTRACTOR, HOMEOWNER) to Spring Security authorities
    - SimpleGrantedAuthority represents a single permission/role

    This involves advanced concepts: interface implementation, entity-to-DTO conversion,
    security authority mapping, and Spring Security integration patterns.
*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /*
        Repository Injection - SPRING FRAMEWORK FEATURE:
        UserRepository injected by Spring's dependency injection container.
        Used to load user data from the database during authentication.
    */
    private final UserRepository userRepository;

    /**
     * Constructor for dependency injection of repository.
     *
     * @param userRepository Repository for accessing user data from database
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by username (email in our case) for Spring Security authentication.
     * This method is automatically called by Spring Security during authentication process.
     *
     * AUTHENTICATION FLOW:
     * 1. User attempts to log in with email/password
     * 2. Spring Security calls this method with the email
     * 3. We load User entity from database
     * 4. Convert User entity to Spring Security UserDetails object
     * 5. Spring Security uses UserDetails for password verification and authorization
     *
     * ENTITY CONVERSION PROCESS:
     * Custom User Entity -> Spring Security UserDetails Object
     * - Email becomes username
     * - Password remains as encoded password hash
     * - UserRole enum becomes SimpleGrantedAuthority with "ROLE_" prefix
     *
     * @param email The email address (username) to load user details for
     * @return UserDetails object containing user authentication and authorization data
     * @throws UsernameNotFoundException if no user found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        /*
            Database User Lookup - OPTIONAL HANDLING PATTERN:

            userRepository.findByEmail(email) returns Optional<User>
            .orElseThrow() handles the case where no user is found
            Throws UsernameNotFoundException (Spring Security standard exception)

            This is Java 8+ Optional pattern for null-safe database operations.
        */
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        /*
            Entity to UserDetails Conversion - SPRING SECURITY INTEGRATION:

            Creates Spring Security's User object (different from our domain User!)
            Full class name: org.springframework.security.core.userdetails.User

            Constructor parameters:
            1. username: User's email (our unique identifier)
            2. password: Encoded password hash from database
            3. authorities: List of roles/permissions for authorization

            Authority Mapping:
            UserRole.CONTRACTOR -> "ROLE_CONTRACTOR"
            UserRole.HOMEOWNER -> "ROLE_HOMEOWNER"
            UserRole.ADMIN -> "ROLE_ADMIN"

            The "ROLE_" prefix is Spring Security convention for role-based authorization.
        */
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),                    // Username (email in our system)
                user.getPassword(),                 // Encoded password hash
                // Convert UserRole enum to Spring Security authority with "ROLE_" prefix
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    /*
        Spring Security Authority System:

        SimpleGrantedAuthority represents a single permission/role that a user has.
        The "ROLE_" prefix is Spring Security convention that enables:

        - @PreAuthorize("hasRole('CONTRACTOR')") annotations (auto-strips ROLE_ prefix)
        - @Secured("ROLE_CONTRACTOR") annotations (requires full ROLE_ prefix)
        - hasRole() and hasAuthority() expressions in security configurations

        Our mapping:
        - User.role = CONTRACTOR -> "ROLE_CONTRACTOR" authority
        - User.role = HOMEOWNER -> "ROLE_HOMEOWNER" authority
        - User.role = ADMIN -> "ROLE_ADMIN" authority

        This allows role-based access control throughout the application:
        - Controllers can check user roles
        - Methods can be secured by role
        - UI can show/hide features based on roles
    */
}