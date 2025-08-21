package org.codeacademy.baltaragisapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple unit test that runs with H2 for local development.
 * This test uses the default profile (not testcontainers) to use H2.
 */
@SpringBootTest
@ActiveProfiles("dev")
class LocalUnitTest {

    @Test
    void contextLoads() {
        // Simple test to verify Spring context loads with H2
        assertThat(true).isTrue();
    }
}
