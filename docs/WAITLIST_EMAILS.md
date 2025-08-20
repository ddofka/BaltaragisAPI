# Waitlist Email Notification System

## Overview

The waitlist email notification system automatically sends emails to subscribers when products come back in stock. This system is designed to work in both development and production environments.

## Features

- **Automatic Notifications**: Sends emails when product quantity increases from 0 to >0 or when unpublished products are published with stock
- **Idempotent**: Each subscriber receives only one notification per stock event
- **Localized**: Supports English (en-US) and Lithuanian (lt-LT) email templates
- **Configurable**: Environment-driven SMTP configuration
- **Development-Friendly**: Uses MailHog for local email testing

## How It Works

### Trigger Conditions

The system sends notifications when:

1. **Quantity Increase**: Product quantity changes from 0 to >0
2. **Publication with Stock**: Unpublished product is set to published with stock >0

### Notification Flow

1. Product is updated via admin API
2. `WaitlistNotificationService` checks if notifications should be sent
3. Finds all waitlist subscribers who haven't been notified yet
4. Sends localized email to each subscriber
5. Marks subscribers as notified by setting `notified_at` timestamp

### Idempotency

- Each waitlist entry has a `notified_at` field
- Once notified, subscribers won't receive duplicate emails for the same stock event
- New stock events (e.g., going out of stock and back in stock) will trigger new notifications

## Development Setup

### MailHog Installation

MailHog is a development SMTP server that captures emails for testing.

#### macOS (using Homebrew)
```bash
brew install mailhog
```

#### Docker
```bash
docker run -d --name mailhog -p 1025:1025 -p 8025:8025 mailhog/mailhog
```

#### Manual Download
Download from [MailHog releases](https://github.com/mailhog/MailHog/releases)

### Starting MailHog

```bash
# If installed via Homebrew
mailhog

# If using Docker
docker start mailhog

# If downloaded manually
./MailHog
```

### Accessing MailHog

- **SMTP Server**: localhost:1025
- **Web Interface**: http://localhost:8025

## Configuration

### Development Profile (`application-dev.yml`)

```yaml
spring:
  mail:
    host: localhost
    port: 1025
    username: 
    password: 
    properties:
      mail:
        smtp:
          auth: false
          starttls:
            enable: false
```

### Production Environment Variables

```bash
export SMTP_HOST=smtp.gmail.com
export SMTP_PORT=587
export SMTP_USERNAME=your-email@gmail.com
export SMTP_PASSWORD=your-app-password
export SMTP_AUTH=true
export SMTP_STARTTLS=true
```

## Email Templates

### Supported Locales

- **en-US**: English (United States)
- **lt-LT**: Lithuanian (Lithuania)

### Template Keys

- `email.waitlist.subject` - Email subject line
- `email.waitlist.greeting` - Opening greeting
- `email.waitlist.product_back_in_stock` - Main message with product name placeholder
- `email.waitlist.purchase_link` - Purchase link with URL placeholder
- `email.waitlist.regards` - Closing regards
- `email.waitlist.team` - Team signature

### Template Example (English)

```
Hello!

The product "Sunset Print" is back in stock!

You can purchase it here: http://localhost:8080/products/sunset-print

Best regards,
Baltaragis team
```

## API Integration

### Product Updates

The system automatically integrates with the existing admin product API:

- `PUT /api/v1/admin/products/{id}` - Triggers notifications on quantity/published status changes
- `POST /api/v1/admin/products` - Triggers notifications for new products with stock

### Waitlist Management

- `POST /api/v1/products/{slug}/waitlist` - Public endpoint for joining waitlists (unchanged)
- Existing waitlist functionality remains unchanged

## Testing

### Unit Tests

```bash
# Run all tests
./mvnw test

# Run specific test classes
./mvnw test -Dtest=EmailServiceTest
./mvnw test -Dtest=WaitlistNotificationServiceTest
```

### Integration Testing

1. Start the application with dev profile
2. Start MailHog
3. Create a product with quantity 0
4. Add email to waitlist via public API
5. Update product quantity to >0 via admin API
6. Check MailHog web interface for received email

### Manual Testing

```bash
# 1. Start MailHog
mailhog

# 2. Start application
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# 3. Create product with 0 stock
curl -X POST http://localhost:8080/api/v1/admin/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Product",
    "slug": "test-product",
    "quantity": 0,
    "isPublished": true
  }'

# 4. Add to waitlist
curl -X POST http://localhost:8080/api/v1/products/test-product/waitlist \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com"}'

# 5. Update product to have stock
curl -X PUT http://localhost:8080/api/v1/admin/products/1 \
  -H "Content-Type: application/json" \
  -d '{"quantity": 5}'

# 6. Check MailHog at http://localhost:8025
```

## Monitoring and Logging

### Log Messages

The system logs key events:

```
INFO  - Product test-product is back in stock, sending waitlist notifications
INFO  - Found 2 waitlist entries to notify for product test-product
INFO  - Waitlist notification sent and marked as notified for email: user@example.com
WARN  - Failed to send waitlist notification for email: invalid@example.com
```

### Health Checks

Email service health can be monitored via Spring Boot Actuator endpoints.

## Future Enhancements

- **User Preferences**: Store user's preferred locale in waitlist entries
- **Email Templates**: HTML email templates with rich formatting
- **Retry Logic**: Automatic retry for failed email deliveries
- **Email Queue**: Asynchronous email processing for better performance
- **Analytics**: Track email open rates and click-through rates
- **Unsubscribe**: Allow users to unsubscribe from waitlist notifications

## Troubleshooting

### Common Issues

1. **Emails not sending**: Check MailHog is running and accessible on port 1025
2. **SMTP errors**: Verify SMTP configuration in application properties
3. **Translation errors**: Ensure email translations are present in the database
4. **Notifications not triggering**: Check product quantity and published status changes

### Debug Mode

Enable debug logging for email services:

```yaml
logging:
  level:
    org.codeacademy.baltaragisapi.service.EmailService: DEBUG
    org.codeacademy.baltaragisapi.service.WaitlistNotificationService: DEBUG
```
