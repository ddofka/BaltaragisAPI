package org.codeacademy.baltaragisapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple unit test that runs with H2 for local development.
 * This test does not extend AbstractMySQLIntegrationTest and will use H2 locally.
 */
@SpringBootTest
class LocalUnitTest {

    @Test
    void contextLoads() {
        // Simple test to verify Spring context loads with H2
        assertThat(true).isTrue();
    }
}
