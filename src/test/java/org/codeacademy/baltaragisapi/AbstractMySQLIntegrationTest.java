package org.codeacademy.baltaragisapi;

import org.codeacademy.baltaragisapi.config.TestcontainersConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Abstract base class for integration tests using MySQL via Testcontainers.
 * 
 * This class sets up:
 * - MySQL container via Testcontainers
 * - testcontainers profile for MySQL-specific configuration
 * - Flyway migrations against MySQL
 * 
 * Tests extending this class will run against a real MySQL database
 * instead of H2, ensuring production-like behavior in CI.
 */
@SpringBootTest
@ActiveProfiles("testcontainers")
@Import(TestcontainersConfiguration.class)
@Testcontainers
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=validate",
    "spring.flyway.enabled=true"
})
public abstract class AbstractMySQLIntegrationTest {
    // This class provides the configuration for MySQL integration tests
    // Concrete test classes should extend this to inherit the MySQL setup
}
