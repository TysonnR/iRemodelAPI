package com.iremodelapi.security;

import com.iremodelapi.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT (JSON Web Token) Service for token generation, validation, and parsing.
 * Handles cryptographic signing/verification of JWT tokens using HMAC-SHA256 algorithm.
 * Provides methods for creating tokens, extracting claims, and validating token authenticity.
 *
 * IMPORTANT ACKNOWLEDGMENT:
 * Most of this code is NOT my original work. This implementation is based on:
 * - YouTube crash course video on Spring Security + JWT authentication
 * - AI assistance to adapt the JWT handling code for this specific project
 * - JJWT library documentation and examples
 *
 * I had no prior knowledge of JWT standards, cryptographic signing, or token-based
 * authentication before this project, and insufficient time to learn these complex
 * security concepts from scratch. This represents advanced enterprise security implementation that is
 * well beyond my basic Java skills and Intro CS coursework but I was determined to not let this hold me back,
 * while also diving deep into some complex concepts!
 *
 * @author Tyson - with significant external learning resources (ProgrammingWithMosh on Youtube, Claude.ai,
 * JJWT library documentation and examples)
 * @date 5/28/2025
 */
/*
    ENTERPRISE SECURITY & CRYPTOGRAPHY FEATURES (Far Beyond Basic Java):

    @Service: Marks this as a Spring service component for dependency injection

    JWT (JSON Web Token): Industry standard for secure information transmission
    - Header: Token type and signing algorithm
    - Payload: Claims (user data, expiration, etc.)
    - Signature: Cryptographic signature to verify authenticity

    JJWT Library: Professional JWT implementation for Java
    - Handles JWT creation, parsing, and validation
    - Provides cryptographic signing and verification
    - Implements JWT and JWS (JSON Web Signature) standards

    HMAC-SHA256: Cryptographic algorithm for message authentication
    - Uses secret key to generate/verify digital signatures
    - Ensures token hasn't been tampered with
    - Industry standard for JWT signing

    This involves advanced concepts: cryptography, digital signatures, time-based
    validation, functional programming, and security standards compliance.
*/
@Service
public class JwtService
{
    /*
        Configuration Injection - SPRING FRAMEWORK FEATURE:
        JwtProperties contains externalized configuration (secret key, expiration time)
        Injected by Spring's dependency injection container.
    */
    private final JwtProperties jwtProperties;

    /**
     * Constructor for dependency injection of JWT configuration properties.
     *
     * @param jwtProperties Configuration object containing JWT secret and expiration settings
     */
    public JwtService(JwtProperties jwtProperties)
    {
        this.jwtProperties = jwtProperties;
    }

    /**
     * Generates a JWT token for a user with default claims.
     * Convenience method that calls the full generateToken method with empty extra claims.
     *
     * @param userDetails Spring Security UserDetails containing user information
     * @return JWT token string ready for HTTP Authorization header
     */
    public String generateToken(UserDetails userDetails)
    {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token with custom claims and user information.
     * Creates a digitally signed token containing user identity and expiration.
     *
     * JWT STRUCTURE CREATED:
     * - Header: {"alg":"HS256","typ":"JWT"}
     * - Payload: {custom claims + subject + issued time + expiration}
     * - Signature: HMAC-SHA256 signature for verification
     *
     * @param extraClaims Additional custom claims to include in token payload
     * @param userDetails Spring Security UserDetails containing user information
     * @return Complete JWT token string (header.payload.signature)
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails)
    {
        /*
            JJWT Builder Pattern - FLUENT API DESIGN:
            Uses method chaining to build JWT step by step:
            1. setClaims() - Add custom data to token
            2. setSubject() - Set the user identifier (username/email)
            3. setIssuedAt() - Set token creation timestamp
            4. setExpiration() - Set when token expires
            5. signWith() - Apply cryptographic signature
            6. compact() - Generate final token string
        */
        return Jwts.builder()
                .setClaims(extraClaims)                    // Custom claims (roles, permissions, etc.)
                .setSubject(userDetails.getUsername())     // Username as token subject
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Current timestamp
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration())) // Expiration time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Cryptographic signature
                .compact();                                // Generate final token string
    }

    /**
     * Validates a JWT token against user details.
     * Checks both username match and token expiration.
     *
     * VALIDATION PROCESS:
     * 1. Extract username from token claims
     * 2. Compare with provided UserDetails username
     * 3. Check if token has expired
     * 4. Return true only if both validations pass
     *
     * @param token JWT token string to validate
     * @param userDetails Expected user details to validate against
     * @return true if token is valid and not expired, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extracts the username (subject) from a JWT token.
     * Uses functional programming to extract specific claim from token.
     *
     * @param token JWT token string to parse
     * @return Username/email stored in token subject claim
     */
    public String extractUsername(String token)
    {
        // Claims::getSubject is a method reference (functional programming)
        // Equivalent to: claims -> claims.getSubject()
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Checks if a JWT token has expired.
     * Compares token expiration time with current time.
     *
     * @param token JWT token string to check
     * @return true if token has expired, false if still valid
     */
    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token JWT token string to parse
     * @return Date when token expires
     */
    private Date extractExpiration(String token)
    {
        // Claims::getExpiration is a method reference (functional programming)
        return extractClaim(token, Claims::getExpiration);
    }

    /*
        Generic Claim Extraction - ADVANCED JAVA FUNCTIONAL PROGRAMMING:

        Function<Claims, T> is a Java 8+ functional interface:
        - Takes Claims object as input
        - Returns type T as output
        - claimsResolver.apply(claims) calls the function

        This allows extracting any claim type using method references:
        - Claims::getSubject returns String
        - Claims::getExpiration returns Date
        - Claims::getIssuedAt returns Date
        - etc.

        This is advanced functional programming - using functions as parameters!
    */

    /**
     * Generic method to extract any claim from a JWT token using functional programming.
     * Uses a function parameter to specify which claim to extract and how to process it.
     *
     * FUNCTIONAL PROGRAMMING PATTERN:
     * - Function<Claims, T> represents a function that takes Claims and returns type T
     * - Method references like Claims::getSubject are passed as functions
     * - claimsResolver.apply(claims) executes the function on the claims
     *
     * @param <T> The type of claim to extract (String, Date, etc.)
     * @param token JWT token string to parse
     * @param claimsResolver Function that specifies which claim to extract
     * @return The extracted claim value of type T
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);  // Apply the function to extract specific claim
    }

    /**
     * Parses a JWT token and extracts all claims from its payload.
     * Verifies the token signature during parsing to ensure authenticity.
     *
     * JWT PARSING PROCESS:
     * 1. Get the signing key for verification
     * 2. Parse and verify the token signature
     * 3. Extract the claims (payload) from verified token
     * 4. Return Claims object containing all token data
     *
     * @param token JWT token string to parse
     * @return Claims object containing all token payload data
     * @throws JwtException if token is invalid, expired, or tampered with
     */
    private Claims extractAllClaims(String token)
    {
        /*
            JJWT Parser - CRYPTOGRAPHIC TOKEN VERIFICATION:
            1. parserBuilder() - Create parser builder
            2. setSigningKey() - Set key for signature verification
            3. build() - Create the parser
            4. parseClaimsJws() - Parse and verify JWT signature
            5. getBody() - Extract claims payload after verification

            If signature verification fails, this throws an exception!
        */
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())    // Key for signature verification
                .build()                           // Build the parser
                .parseClaimsJws(token)            // Parse and verify token
                .getBody();                       // Extract verified claims
    }

    /**
     * Generates the cryptographic signing key from the configured secret.
     * Converts the secret string to bytes and creates an HMAC-SHA key.
     *
     * CRYPTOGRAPHIC KEY GENERATION:
     * 1. Get secret string from configuration
     * 2. Convert to UTF-8 byte array
     * 3. Generate HMAC-SHA key for signing/verification
     * 4. Return SecretKey for JWT operations
     *
     * @return SecretKey for HMAC-SHA256 signing and verification
     */
    private SecretKey getSigningKey()
    {
        // Convert secret string to bytes using UTF-8 encoding
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);

        // Generate HMAC-SHA key from bytes for cryptographic operations
        return Keys.hmacShaKeyFor(keyBytes);
    }
}