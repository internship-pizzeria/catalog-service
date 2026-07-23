# Catalog Service

Microservice for managing restaurant locations, products, and menus in a pizza restaurant chain. Built with Spring Boot 4.0.7 and Java 25.

## Overview

This service manages:

- **Locations** — physical restaurant storefronts (address, status, timezone)
- **Products** — global pizza menu items (name, description, price)
- **Ingredients** — ingredients with per-location availability
- **Menus** — assembled per-location by filtering products based on unavailable ingredients

It exposes both public REST APIs and internal endpoints for inter-service communication.

## Tech Stack

- Java 25
- Spring Boot 4.0.7
- Spring Cloud 2025.1.2
- Spring Data JPA + Hibernate
- PostgreSQL
- Spring Data Redis (caching)
- Lombok
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 + Mockito

## Project Structure

```
src/main/java/com/pizzera/catalog_service/
├── CatalogServiceApplication.java
├── config/
│   └── RedisConfig.java                  # Cache configuration (TTL per cache)
├── security/
│   ├── LocationAuthFilter.java           # X-User-Id header validation
│   └── LocationContext.java              # Current location from request context
├── location/
│   ├── Location.java                     # Entity (package-private)
│   ├── LocationStatus.java               # Enum
│   ├── LocationRepository.java           # Repository (package-private)
│   ├── LocationService.java              # Service
│   ├── LocationController.java           # REST controller
│   ├── LocationResponse.java             # DTO with static from()
│   └── LocationNotFoundException.java
├── product/
│   ├── Product.java                      # Entity (package-private)
│   ├── ProductIngredient.java            # Join entity (package-private)
│   ├── ProductRepository.java            # Repository (package-private)
│   ├── ProductService.java               # Service
│   ├── ProductController.java            # Public REST controller
│   ├── InternalProductController.java    # Internal REST controller
│   ├── ProductResponse.java              # DTO with static from()
│   ├── InternalProductResponse.java      # DTO with static from()
│   ├── ProductWithIngredientsResponse.java # DTO with static from()
│   ├── CreateProductRequest.java         # Request DTO
│   └── ProductNotFoundException.java
├── ingredient/
│   ├── Ingredient.java                   # Entity (public — cross-package ref)
│   ├── IngredientCategory.java           # Enum (public)
│   ├── LocationIngredient.java           # Per-location availability (package-private)
│   ├── IngredientRepository.java         # Repository (package-private)
│   ├── LocationIngredientRepository.java # Repository (package-private)
│   ├── IngredientService.java            # Service
│   ├── InternalIngredientController.java # Internal REST controller
│   ├── LocationIngredientResponse.java   # DTO with static from()
│   └── IngredientNotFoundException.java
└── menu/
    ├── MenuService.java                  # Assembles per-location menu
    ├── MenuController.java               # REST controller
    └── MenuResponse.java                 # DTO
```

## Architecture & Encapsulation

The codebase follows package-private encapsulation:

- **Entities and repositories** are package-private — not accessible outside their package
- **DTOs** expose only canonical constructors (standard types); entity→DTO conversion via `static from()` methods within the same package
- **Services** are public and inject other services (not repositories) for cross-package access
- **Controllers** return only DTOs, never entities

```
Package visibility:
  public:             Services, Controllers, DTOs, Exceptions, Ingredient, enums
  package-private:    Entities, Repositories
```

Cross-package dependency example: `MenuService` uses `ProductService`, `LocationService`, and `IngredientService` — never their repositories directly.

## Prerequisites

- Java 25+
- Maven (or use `./mvnw`)
- PostgreSQL running on `localhost:5433`
- Redis running on `localhost:6379`

## Configuration

1. Copy `.env.example` to `.env`:

```bash
cp .env.example .env
```

2. Configure environment variables (see `.env.example` for all available options):

```
DB_URL, DB_USER, DB_PASSWORD    # PostgreSQL connection
REDIS_HOST, REDIS_PORT          # Redis connection
REDIS_MENU_TTL_MINUTES          # Menu cache TTL (default: 5)
REDIS_LOCATIONS_TTL_HOURS       # Locations cache TTL (default: 24)
PORT                            # Server port (default: 8081)
```

### Key Configuration (`application.properties`)

| Property | Value | Description |
|----------|-------|-------------|
| `server.port` | `${PORT:8081}` | Server port |
| `spring.datasource.url` | `${DB_URL}` | PostgreSQL connection URL |
| `spring.jpa.hibernate.ddl-auto` | `update` | Auto-create/update schema |
| `spring.data.web.pageable.max-page-size` | `50` | Max page size for pagination |
| `spring.mvc.problem-details.enabled` | `true` | RFC 7807 error responses |

## Running

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Using Maven
mvn spring-boot:run
```

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
| `GET` | `/api/v1/products/{id}` | Get a product by ID |
| `POST` | `/api/v1/products` | Create a new product |

#### Menu

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/menu` | Get filtered menu for a location |

**Query Parameters:**
- `locationId` — location ID

**Behavior:** Returns only products whose ingredients are all available at the given location. Products with unavailable ingredients are filtered out.

### Internal APIs (for inter-service communication)

Requires `X-User-Id` header with the calling service's location ID.

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/internal/products/details` | Batch-fetch product details by IDs |
| `GET` | `/api/v1/internal/locations/{locationId}/ingredients` | Get ingredient availability for a location |
| `PATCH` | `/api/v1/internal/locations/{locationId}/ingredients/{ingredientId}` | Toggle ingredient availability |

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
| `ingredients` | List\<ProductIngredient\> | OneToMany |
| `createdAt` | Instant | Auto-generated |

### LocationIngredient

| Field | Type | Constraints |
|-------|------|-------------|
| `id` | Long | Auto-generated |
| `locationId` | Long | Required |
| `ingredient` | Ingredient | ManyToOne |
| `available` | boolean | Required (default: true) |

### LocationStatus Enum

`ACTIVE`, `OUT_OF_WORKING_HOURS`, `TEMPORARILY_CLOSED`, `IN_RENOVATION`, `PERMANENTLY_CLOSED`

### IngredientCategory Enum

`MEAT`, `VEGETABLE`, `CHEESE`, `SAUCE`, `OTHER`

## Caching (Redis)

| Cache | Key | TTL | Eviction |
|-------|-----|-----|----------|
| `menu` | `locationId` | 5 min (configurable) | On ingredient toggle (per-location) or product creation (all entries) |
| `locations` | none | 24 h (configurable) | — |

## Testing

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ProductServiceTest
```

Tests use JUnit 5 with Mockito for pure unit testing (no Spring context).

## Swagger UI

```
http://localhost:8081/swagger-ui.html
```
