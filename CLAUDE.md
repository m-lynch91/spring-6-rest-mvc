# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3 / Spring Framework 6 REST API application built as part of the "Spring Framework 6: Beginner to Guru" course. It demonstrates a complete MVC architecture with JPA/Hibernate persistence, RESTful endpoints, and comprehensive testing.

**Key Technologies:**
- Java 21
- Spring Boot 3.4.0
- Spring Data JPA with Hibernate
- PostgreSQL (production) / H2 (testing)
- Flyway for database migrations
- MapStruct for DTO/Entity mapping
- Lombok for boilerplate reduction
- Testcontainers for integration testing

## Build and Run Commands

### Build and Test
```bash
# Clean and compile (generates MapStruct implementations)
mvn clean compile

# Run all tests (unit tests only)
mvn test

# Run integration tests (uses maven-failsafe-plugin)
# Integration test classes must end with IT.java, IT*.java, or *IT.java
mvn verify

# Run all tests (both unit and integration)
mvn clean verify

# Run a single test class
mvn test -Dtest=BeerControllerTest

# Run a single integration test
mvn verify -Dit.test=BeerControllerIT

# Run a specific test method
mvn test -Dtest=BeerControllerTest#testMethod
```

### Run Application
```bash
# Run Spring Boot application
mvn spring-boot:run

# Build JAR
mvn clean package

# Run JAR
java -jar target/spring-6-rest-mvc-0.0.1-SNAPSHOT.jar
```

### Code Quality
```bash
# Check code formatting (Spring Java Format)
mvn spring-javaformat:validate

# Apply code formatting
mvn spring-javaformat:apply

# Run checkstyle
mvn checkstyle:check
```

### Database
```bash
# Start PostgreSQL with Docker Compose
docker-compose up -d

# Stop PostgreSQL
docker-compose down
```

## Architecture

### Layered Architecture Pattern

The application follows a standard Spring MVC layered architecture:

**Controllers** (`controllers/`) → **Services** (`services/`) → **Repositories** (`repositories/`) → **Entities** (`entities/`)
                                        ↕ (MapStruct)
                                      **DTOs** (`model/`)

### Key Architectural Components

#### 1. Dual Service Implementation Pattern
Services have **two implementations** for the same interface:
- `*ServiceImpl` - In-memory implementation (mainly for testing/learning)
- `*ServiceJPA` - JPA-backed implementation marked with `@Primary`

Example:
- `BeerService` (interface)
  - `BeerServiceImpl` - In-memory Map-based storage
  - `BeerServiceJPA` - Database-backed with Spring Data JPA (active implementation)

#### 2. Entity-DTO Separation
- **Entities** (`entities/`) - JPA entities with Hibernate annotations, used internally
- **DTOs** (`model/`) - Data Transfer Objects exposed via REST API
- **MapStruct mappers** (`mappers/`) - Automatic mapping between entities and DTOs
  - Generated implementations appear in `target/generated-sources/annotations/`
  - Configured with `mapstruct.defaultComponentModel=spring` in `pom.xml`

#### 3. JPA Relationship Mappings
Complex bidirectional relationships exist between entities:
- `Customer` ↔ `BeerOrder` (One-to-Many / Many-to-One)
- `BeerOrder` ↔ `BeerOrderLine` (One-to-Many / Many-to-One)
- `Beer` ↔ `BeerOrderLine` (One-to-Many / Many-to-One)

**Important:** Association helper methods maintain both sides of bidirectional relationships. See `BeerOrder.setCustomer()` which automatically adds the order to the customer's collection.

#### 4. Database Schema Management
- **Flyway migrations** in `src/main/resources/db/migration/`
- Versioned SQL scripts (V1__, V2__, etc.)
- Currently disabled by default (`spring.flyway.enabled=false`)
- Schema generation tools available in `application.properties` (commented out)

### REST API Structure

All endpoints follow REST conventions:
- **Base paths:** `/api/v1/beers`, `/api/v1/customers`
- **HTTP verbs:** GET (list/single), POST (create), PUT (full update), PATCH (partial update), DELETE
- **Response codes:** 200 OK, 201 Created, 204 No Content, 404 Not Found

#### Pagination and Filtering
The Beer API supports:
- Pagination: `?pageNumber=1&pageSize=25` (defaults: page 0, size 25, max 1000)
- Filtering: `?beerName=IPA&beerStyle=IPA`
- Inventory toggle: `?showInventory=false` (hides `quantityOnHand`)
- Results sorted by `beerName` ascending

### Testing Strategy

**Unit Tests** (`*Test.java`):
- Use `@WebMvcTest` for controller tests with mocked services
- Use `@DataJpaTest` for repository tests with in-memory H2

**Integration Tests** (`*IT.java`):
- Use `@SpringBootTest` with full application context
- Use `@Transactional` and `@Rollback` for test isolation
- Use Testcontainers for PostgreSQL integration tests
- Use MockMvc for HTTP endpoint testing

### Data Initialization

**BootstrapData** (`util/BootstrapData.java`):
- Implements `CommandLineRunner` to load initial data on startup
- Loads sample beers from CSV (`src/main/resources/csvdata/beers.csv`)
- Creates sample customers
- Only runs if database has fewer than 10 beers

## Development Notes

### MapStruct Configuration
- Lombok must be processed **before** MapStruct in the annotation processor chain
- The `lombok-mapstruct-binding` dependency ensures compatibility
- Regenerate mappers after entity/DTO changes: `mvn clean compile`

### Common Gotchas
1. **Integration tests not running?** Ensure class name ends with `IT.java` and use `mvn verify` not `mvn test`
2. **MapStruct not working?** Check `target/generated-sources/annotations/` for generated implementations
3. **Flyway disabled:** The app uses JPA auto-DDL by default. Enable Flyway with `spring.flyway.enabled=true`
4. **Docker Compose disabled:** Set `spring.docker.compose.enabled=true` to auto-start PostgreSQL

### Entity Versioning
All entities use optimistic locking with `@Version` fields to prevent concurrent update conflicts.

### UUID Primary Keys
All entities use UUID primary keys with:
- `@UuidGenerator` for generation
- `@JdbcTypeCode(SqlTypes.CHAR)` for database storage
- `varchar(36)` column definition
