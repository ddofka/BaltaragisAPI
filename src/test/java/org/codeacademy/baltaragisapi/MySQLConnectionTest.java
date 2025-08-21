package org.codeacademy.baltaragisapi;

import org.codeacademy.baltaragisapi.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple test to verify MySQL Testcontainers setup works in CI.
 * Uses minimal configuration to test database connectivity only.
 */
@SpringBootTest
@ActiveProfiles("testcontainers")
@Import(TestcontainersConfiguration.class)
@Testcontainers
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false",
    "spring.datasource.url=",  // Let Testcontainers provide the URL
    "spring.datasource.username=",  // Let Testcontainers provide the username
    "spring.datasource.password="   // Let Testcontainers provide the password
})
class MySQLConnectionTest {

    @Test
    void contextLoadsWithMySQLContainer() {
        // This test verifies that:
        // 1. MySQL container starts successfully
        // 2. Spring Boot application can connect to it
        // 3. Basic JPA configuration works with MySQL
        assertThat(true).isTrue(); // Test passes if context loads
    }
}
