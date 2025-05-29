package com.iremodelapi.repository;

import com.iremodelapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity database operations.
 * Extends Spring Data JPA's JpaRepository to provide automatic CRUD operations
 * plus custom finder methods for user authentication and search functionality.
 *
 * SPRING FRAMEWORK MAGIC: Spring automatically generates the implementation class
 * at runtime - you never write the actual implementation code!
 *
 * This repository demonstrates advanced features: Optional return types, boolean methods,
 * and complex JPQL string manipulation functions.
 *
 * @author Tyson Ringelstetter
 * @date 05/28/2025
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
     /*
        Optional Return Types JAVA 8+ FEATURE:

        Optional<User> is a Java 8+ container that can hold either a User or be empty.
        This prevents NullPointerException and forces explicit null handling.

        Spring Data JPA automatically wraps single results in Optional when:
        - Method returns Optional<EntityType>
        - Result might not exist (like unique field searches)

        This is safer than returning null and represents modern Java best practices.
    */
    /**
     * Finds a user by their unique email address.
     * Returns Optional to handle the case where no user exists with that email.
     * Essential for authentication and user lookup functionality.
     *
     * Spring converts method name to: WHERE email = :email
     *
     * @param email The email address to search for
     * @return Optional containing the user if found, or empty Optional if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email address.
     * More efficient than findByEmail() when you only need existence check.
     * Useful for validation during user registration.
     *
     * Spring converts method name to: COUNT query returning boolean
     *
     * @param email The email address to check for existence
     * @return true if a user exists with this email, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     /**
     * Finds all users with a specific role in the system.
     * Spring converts method name to: WHERE role = :role
     * Works with enum values automatically!
     *
     * @param role The user role to filter by (CONTRACTOR, HOMEOWNER, ADMIN)
     * @return List of users with the specified role
     */
    List<User> findByRole(User.UserRole role);

    /**
     * Finds users by partial name match using case-insensitive search.
     * Searches both first and last names combined as a single string.
     * Useful for user search and autocomplete functionality.
     *
     * Example: searching "john doe" will match "John Doe", "JOHN DOE", "john doe", etc.
     * Example: searching "joh" will match "John Smith", "Johnny Doe", etc.
     *
     * @param name The name pattern to search for (case-insensitive)
     * @return List of users whose combined first and last names contain the search pattern
     */
    @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContainingIgnoreCase(String name);
}