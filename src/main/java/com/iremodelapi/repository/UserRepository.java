package com.iremodelapi.repository;

import com.iremodelapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Provides methods to retrieve and manipulate User entities in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their email address.
     *
     * @param email The email to search for
     * @return An Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email address.
     *
     * @param email The email to check
     * @return true if a user exists with this email, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find all users with a specific role.
     *
     * @param role The role to filter by (CONTRACTOR, HOMEOWNER, etc.)
     * @return A list of users with the specified role
     */
    List<User> findByRole(User.UserRole role);

    /**
     * Find users by partial name match (case insensitive).
     * Useful for search functionality.
     *
     * @param name The name pattern to search for
     * @return A list of users matching the name pattern
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContainingIgnoreCase(String name);
}