# Catalog Service

A microservice for managing restaurant locations and product catalogs in a pizza restaurant chain. Built with Spring Boot 4.0.7 and Java 25.

## Overview

This service manages:

- **Locations** — physical restaurant storefronts (address, status, timezone)
- **Products** — pizza menu items tied to specific locations (name, description, price)
- **Menus** — the product catalog for a given location

It exposes both public REST APIs and internal endpoints for inter-service communication within a Spring Cloud ecosystem.

## Tech Stack

- Java 25
- Spring Boot 4.0.7
- Spring Cloud 2025.1.2
- Spring Data JPA + Hibernate
- PostgreSQL
- Lombok
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 + Mockito

## Project Structure

```
src/main/java/com/pizzera/catalog_service/
├── CatalogServiceApplication.java
├── product/
│   ├── Product.java                  # Entity
│   ├── ProductRepository.java        # Repository
│   ├── ProductService.java           # Service
│   ├── MenuService.java              # Menu logic
│   ├── ProductController.java        # Public REST controller
│   ├── InternalProductController.java # Internal REST controller
│   ├── MenuController.java           # Menu REST controller
│   ├── ProductResponse.java          # DTO
│   ├── InternalProductResponse.java  # DTO (internal)
│   └── ProductNotFoundException.java # Custom exception
└── location/
    ├── Location.java                 # Entity
    ├── LocationStatus.java           # Enum
    ├── LocationRepository.java       # Repository
    ├── LocationService.java          # Service
    ├── LocationController.java       # REST controller
    ├── LocationResponse.java         # DTO
    └── LocationNotFoundException.java # Custom exception
```

## Prerequisites

- Java 25+
- Maven (or use `./mvnw`)
- PostgreSQL running on `localhost:5433`

## Configuration

1. Copy `.env.example` to `.env`:

```bash
cp .env.example .env
```

2. Fill in database credentials:

```
DB_URL=jdbc:postgresql://localhost:5433/catalogDB
DB_USER=myuser
DB_PASSWORD=mypassword
```

### Key Configuration (`application.properties`)

| Property | Value | Description |
|----------|-------|-------------|
| `server.port` | `${PORT:8081}` | Server port (default 8081) |
| `spring.datasource.url` | `${DB_URL}` | PostgreSQL connection URL |
| `spring.jpa.hibernate.ddl-auto` | `update` | Auto-create/update schema |
| `spring.sql.init.mode` | `always` | Run seed data on startup |
| `spring.data.web.pageable.max-page-size` | `50` | Max page size for pagination |
| `spring.mvc.problem-details.enabled` | `true` | RFC 7807 error responses |

## Running

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Using Maven
mvn spring-boot:run
```

The application starts on port 8081 by default. Override with the `PORT` environment variable.

## API Endpoints

### Public APIs

#### Locations

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/locations` | List active locations (paginated, optional city filter) |

**Query Parameters:**
- `city` (optional) — filter by city name (case-insensitive substring match)
- `page`, `size`, `sort` — pagination parameters

#### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/products/{id}` | Get a product by ID (scoped to location) |
| `POST` | `/api/v1/products` | Create a new product |

**GET Parameters:**
- `id` (path) — product ID
- `locationId` (query) — location ID

#### Menu

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/menu` | Get full menu for a location |

**Query Parameters:**
- `locationId` — location ID

### Internal APIs (for inter-service communication)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/internal/products/details` | Batch-fetch product details by IDs |

**Request Body:** `List<Long>` (product IDs)

**Response:** `List<InternalProductResponse>` (id, name, price)

## Data Model

### Location

| Field | Type | Constraints |
|-------|------|-------------|
| `id` | Long | Auto-generated |
| `city` | String | Required |
| `postalCode` | String | Optional |
| `street` | String | Required |
| `buildingNumber` | String | Required |
| `countryCode` | String | Required |
| `timezone` | String | Required |
| `status` | LocationStatus | Required (default: ACTIVE) |
| `createdAt` | Instant | Auto-generated |

### Product

| Field | Type | Constraints |
|-------|------|-------------|
| `id` | Long | Auto-generated |
| `name` | String | Required |
| `description` | String | Optional |
| `price` | BigDecimal | Required |
| `location` | Location | Required (ManyToOne) |
| `createdAt` | Instant | Auto-generated |

### LocationStatus Enum

- `ACTIVE`
- `OUT_OF_WORKING_HOURS`
- `TEMPORARILY_CLOSED`
- `IN_RENOVATION`
- `PERMANENTLY_CLOSED`

## Caching

- **Menu cache**: Cached per location ID. Evicted when a new product is created for that location.
- **Locations cache**: Cached globally. Evicted on product creation.

## Testing

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ProductServiceTest
```

Tests use JUnit 5 with Mockito for pure unit testing (no Spring context).

## Swagger UI

Access the OpenAPI documentation at:

```
http://localhost:8081/swagger-ui.html
```
