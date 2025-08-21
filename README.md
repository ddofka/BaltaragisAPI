# Baltaragis API

A Spring Boot e-commerce API for the Baltaragis art and accessories brand, built with modern Java and Spring technologies.

## 🚀 Features

### ✅ Completed Features

#### 1. **Core E-commerce Infrastructure**
- **Product Management**: Full CRUD operations for products with categories, pricing, and inventory
- **Order System**: Order creation, status management, and checkout flow
- **Artist Profile**: Artist information, bio, and social media links
- **Content Pages**: Dynamic page management with Markdown support
- **Photo Management**: Product photos with metadata (dimensions, alt text, sort order)

#### 2. **Internationalization (i18n) Foundation**
- **Multi-language Support**: Lithuanian (LT) and English (EN) locales
- **Smart Locale Detection**: IP country-based default (LT for Lithuania, EN elsewhere)
- **Override Support**: Accept-Language header and X-Locale query/header support
- **Editable Translations**: Admin CRUD operations for UI translations
- **Public API**: `GET /api/v1/i18n/{locale}` endpoint for frontend translation keys

#### 3. **Security & Rate Limiting**
- **JWT Authentication**: Stateless JWT-based auth for admin endpoints
- **Role-Based Access**: ADMIN and EDITOR roles with appropriate permissions
- **Rate Limiting**: IP-based throttling for sensitive endpoints
- **CORS Configuration**: Configured for frontend origins
- **Secure Password Storage**: BCrypt password encoding

#### 4. **Waitlist Email Notification System**
- **Stock Monitoring**: Automatic detection when products come back in stock
- **Email Notifications**: SMTP-based email system using MailHog for development
- **Waitlist Management**: Subscriber tracking with notification history
- **Idempotent Notifications**: Prevents duplicate emails for repeated stock changes
- **Localized Templates**: Email content in user's preferred language

#### 5. **Payments Stub System**
- **Feature Flag Control**: `payments.enabled` configuration toggle
- **Checkout Sessions**: `POST /api/v1/orders/checkout-session` endpoint
- **Stub Implementation**: Development-friendly payment simulation
- **Status Tracking**: Checkout session status management
- **Thymeleaf Integration**: HTML checkout page for payment simulation

#### 6. **Enhanced Seed Data**
- **9 Products Total**: Mix of in-stock (6) and out-of-stock (3) products
- **Product Variety**: Art prints, leather goods, accessories, tech items
- **Realistic Data**: Proper names, descriptions, pricing, and inventory levels
- **Photo Coverage**: 2-3 photos per product with descriptive alt text
- **Category Diversity**: Wallets, cardholders, key fobs, belts, phone cases, notebooks

## 🛠 Technology Stack

- **Java 21** with Spring Boot 3.5.4
- **Spring Data JPA** with Hibernate ORM
- **Spring Security** with JWT authentication (HS256)
- **Spring Boot Mail** with MailHog integration
- **Databases**:
  - H2 (local development)
  - MySQL (CI/production) via Testcontainers
- **Migration**: Flyway for schema versioning
- **Testing**:
  - JUnit 5
  - Testcontainers
  - Spring Boot Test
- **Rate Limiting**: Bucket4j
- **Documentation**: OpenAPI 3 with Swagger UI
- **Build & CI**:
  - Maven
  - GitHub Actions
- **Utilities**:
  - Lombok
  - MapStruct
  - Thymeleaf

## 🚦 Getting Started

### Prerequisites
- Java 21+
- Maven 3.6+
- Docker (for MySQL Testcontainers in CI)

### Development Setup
1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd BaltaragisAPI
   ```

2. **Run the application**
   ```bash
   # Development profile with H2 (includes seed data)
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   
   # MySQL with Testcontainers (requires Docker)
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=testcontainers
   
   # Production profile
   ./mvnw spring-boot:run
   ```

3. **Access the API**
   - **API Documentation**: http://localhost:8080/swagger-ui.html
   - **H2 Console** (dev only): http://localhost:8080/h2-console
   - **Health Check**: http://localhost:8080/actuator/health

### Environment Configuration
```yaml
# application.yml
app:
  base-url: ${BASE_URL:https://www.baltaragis.com}  # For sitemap generation
  media:
    base-path: media
    base-url: http://localhost:8080/media
  payments:
    enabled: ${PAYMENTS_ENABLED:false}
  rate-limit:
    enabled: true
    capacity: 5
    window-minutes: 5

# application-dev.yml
app:
  base-url: http://localhost:8080  # Development URL for sitemap
  payments:
    enabled: true
  rate-limit:
    capacity: 10  # More lenient for development

jwt:
  secret: your-base64-encoded-secret  # Required for production
```

## 📚 API Endpoints

### Authentication
- `POST /api/v1/auth/login` - Obtain JWT access token
  ```json
  {
    "username": "admin",
    "password": "your-password"
  }
  ```

### Public Endpoints
- `GET /api/v1/products` - List published products
- `GET /api/v1/products/{slug}` - Get product details
- `GET /api/v1/artist` - Get artist profile
- `GET /api/v1/pages` - List published pages
- `GET /api/v1/i18n/{locale}` - Get translations for locale
- `POST /api/v1/orders` - Create new order (rate limited)
- `POST /api/v1/products/{slug}/waitlist` - Join waitlist (rate limited)

### SEO Endpoints
- `GET /sitemap.xml` - Dynamic sitemap with all published content
- `GET /robots.txt` - Search engine crawling directives

### Payment Endpoints (when enabled)
- `POST /api/v1/orders/checkout-session` - Create checkout session
- `GET /api/v1/orders/checkout-session/status` - Check session status
- `GET /api/v1/payments/stub-checkout` - Development checkout page

### Admin Endpoints (requires JWT)
- `POST /api/v1/admin/products` - Create product
- `PUT /api/v1/admin/products/{id}` - Update product
- `DELETE /api/v1/admin/products/{id}` - Delete product
- `POST /api/v1/admin/products/{id}/photos/upload` - Upload product photos
- `GET /api/v1/admin/translations` - List translations
- `POST /api/v1/admin/translations` - Create/update translation

## 🗄 Database Schema

### Core Tables
- `product` - Product information, pricing, inventory
- `product_photo` - Product images with metadata
- `order` - Customer orders and status
- `artist_profile` - Artist information and social links
- `page` - Content pages with Markdown support
- `translation` - Internationalization keys and values

### Key Relationships
- Products have multiple photos (one-to-many)
- Orders reference products (many-to-one)
- Translations are key-value pairs with locale

## 🔧 Development Workflow

### Branch Strategy
- **Feature branches**: `feature/feature-name`
- **Pull requests**: Required for all changes
- **CI/CD**: Automated testing with GitHub Actions

### Testing
```bash
# Run all tests with H2 (fast, for local development)
./mvnw test

# Run tests with MySQL via Testcontainers (CI environment)
./mvnw test -Dspring.profiles.active=testcontainers

# Run specific test class
./mvnw test -Dtest=ProductServiceTest
```

### Database Migrations
- **Flyway**: Database version control
- **Development**: `src/main/resources/dev-migration/`
- **Production**: `src/main/resources/db/migration/`

## 🔍 SEO & Search Engine Optimization

The API provides dynamic SEO endpoints that automatically include all published content without requiring frontend rebuilds.

### Sitemap.xml
- **Endpoint**: `GET /sitemap.xml`
- **Content**: Dynamic XML sitemap including:
  - Home page (`/`)
  - Products listing (`/products`)
  - Individual product pages (`/products/{slug}`)
  - Published content pages (`/pages/{slug}`)
- **Features**:
  - Automatic inclusion of newly published content
  - `lastmod` timestamps from database
  - Appropriate `changefreq` and `priority` values
  - Multilingual support with `hreflang` attributes (EN/LT)
  - Cache headers (5-minute TTL + ETag)

### Robots.txt
- **Endpoint**: `GET /robots.txt`
- **Content**: Search engine crawling directives
- **Features**:
  - References sitemap URL
  - Allows all content crawling
  - Cache headers (1-hour TTL + ETag)

### Frontend Integration
The sitemap and robots endpoints are designed to work seamlessly with any frontend:
```html
<!-- Reference in HTML head -->
<link rel="sitemap" type="application/xml" href="/sitemap.xml" />
```

### Configuration
```yaml
app:
  base-url: https://www.baltaragis.com  # Used for generating absolute URLs
```

## 📖 Documentation

- **API Documentation**: [OpenAPI/Swagger UI](http://localhost:8080/swagger-ui.html)
- **i18n Guide**: [docs/i18n.md](docs/i18n.md)
- **Testing Strategy**: [docs/testing.md](docs/testing.md)
- **Waitlist System**: [docs/WAITLIST_EMAILS.md](docs/WAITLIST_EMAILS.md)
- **Media Pipeline**: [MEDIA_PIPELINE.md](MEDIA_PIPELINE.md)

## 🎯 Project Status

### ✅ Completed Milestones
1. **Core Infrastructure** - Basic e-commerce functionality
2. **Internationalization** - Multi-language support with smart detection
3. **Waitlist System** - Stock notification emails
4. **Payment Stub** - Checkout flow preparation
5. **Seed Data Enhancement** - Comprehensive development data
6. **Security** - JWT auth and rate limiting
7. **CI Pipeline** - MySQL testing via Testcontainers
8. **SEO Optimization** - Dynamic sitemap.xml and robots.txt with caching

### 🔄 Next Steps
- **Media Pipeline**: S3/R2 integration for production photo storage
- **Real Payment Integration**: Stripe or similar payment processor
- **User Authentication**: Customer account management
- **Inventory Management**: Advanced stock tracking and alerts

## 🤝 Contributing

1. Create a feature branch from `master`
2. Implement your changes with tests
3. Create a pull request with descriptive title and body
4. Ensure CI passes before requesting review
5. Merge after approval

## 📄 License

This project is proprietary to Baltaragis. All rights reserved.

---

**Built with ❤️ using Spring Boot and modern Java technologies**