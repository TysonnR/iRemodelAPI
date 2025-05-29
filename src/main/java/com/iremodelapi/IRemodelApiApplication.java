package com.iremodelapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main Spring Boot application class for the iRemodel API.
 * Serves as the entry point and configuration hub for the entire enterprise application.
 * Coordinates the startup of all application layers: web, service, repository, and security.
 *
 * SPRING BOOT APPLICATION ARCHITECTURE:
 * This class represents the foundation of the enterprise Spring Boot application:
 * 1. Application startup and Spring context initialization
 * 2. Component scanning and automatic configuration
 * 3. Configuration property binding and externalization
 * 4. Integration of all application layers (web, security, data, business logic)
 * 5. Embedded server startup (Tomcat) and HTTP endpoint exposure
 *.
 *
 * APPLICATION LAYERS COORDINATED:
 * - Web Layer: REST controllers (@RestController) for HTTP API endpoints
 * - Security Layer: JWT authentication and Spring Security configuration
 * - Service Layer: Business logic and transaction management (@Service)
 * - Repository Layer: Spring Data JPA repositories for database operations
 * - Domain Layer: JPA entities with database mapping
 * - Configuration Layer: External properties and framework configuration
 *
 * @author Tyson Ringelstetter
 * @date 05/28/2025
 */
@SpringBootApplication
@EnableConfigurationProperties
public class IRemodelApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IRemodelApiApplication.class, args);
    }
}