# Testing with Testcontainers

This project uses Testcontainers for integration testing against MySQL in CI, while keeping H2 for fast local development.

## Local Development

- **Database**: H2 in-memory database
- **Profile**: `dev` (default)
- **Speed**: Fast startup and test execution
- **Docker**: Not required

## CI Testing

- **Database**: MySQL 8.0 via Testcontainers
- **Profile**: `testcontainers`
- **Purpose**: Production-like database behavior
- **Docker**: Required (available in GitHub Actions)

## Running Tests

### Local Development (H2)
```bash
# Run tests with H2 (default)
./mvnw test

# Or explicitly with dev profile
./mvnw test -Dspring.profiles.active=dev
```

### CI/MySQL Testing (requires Docker)
```bash
# Run tests with MySQL Testcontainers
./mvnw test -Dspring.profiles.active=testcontainers
```

## Configuration

### Test Configuration Files
- `src/test/resources/application-testcontainers.yml` - MySQL Testcontainers config
- `src/test/java/org/codeacademy/baltaragisapi/config/TestcontainersConfiguration.java` - Container setup
- `src/test/java/org/codeacademy/baltaragisapi/AbstractMySQLIntegrationTest.java` - Base test class

### GitHub Actions
- `.github/workflows/ci.yml` - Runs tests with MySQL Testcontainers
- Caches Maven dependencies
- Automatically available Docker environment

## Schema Compatibility

The application uses Flyway migrations that are compatible with both H2 (local) and MySQL (CI):
- H2 for fast local development and unit tests
- MySQL for integration testing and production-like behavior
- Same migration scripts work on both databases

## Integration Test Base Class

Tests that need database connectivity should extend `AbstractMySQLIntegrationTest`:

```java
class MyIntegrationTest extends AbstractMySQLIntegrationTest {
    // Test methods using real MySQL database in CI
    // Uses H2 for local development
}
```

This ensures:
- MySQL container spins up automatically in CI
- Flyway migrations run against MySQL
- Tests run against production-like database behavior
- Local development remains fast with H2
