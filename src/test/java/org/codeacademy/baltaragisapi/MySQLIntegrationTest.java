package org.codeacademy.baltaragisapi;

import org.codeacademy.baltaragisapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Specific MySQL integration test that runs with Testcontainers.
 * This test verifies that the application can start with MySQL and basic operations work.
 */
class MySQLIntegrationTest extends AbstractMySQLIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoadsWithMySQL() {
        // Verify Spring context loads successfully with MySQL
        assertThat(productRepository).isNotNull();
    }

    @Test
    void canQueryMySQLDatabase() {
        // Verify we can perform basic database operations with MySQL
        long count = productRepository.count();
        assertThat(count).isGreaterThanOrEqualTo(0);
    }
}
