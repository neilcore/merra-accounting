# AI Coding Assistant Instructions for Merra Accounting

This document provides essential context for AI tools working with the Merra Accounting codebase.

## Project Architecture

- **Microservices Structure**: Multiple Spring Boot services organized as Maven modules:
  - `auth`: Authentication/authorization service (JWT, OAuth2 with Google)
  - `user`: User account management
  - `organization`: Organization/business entity management
  - `commons`: Shared utilities and models
  - `main`: Main application entry point and configuration

## Key Technical Patterns

### Database

- PostgreSQL with Liquibase for schema management
- Schema name: `merra_schema`
- Migrations in `db/changelog/db.changelog-master.xml`
- JPA/Hibernate with repositories in each module

### Authentication

- JWT-based authentication with access/refresh tokens
- OAuth2 integration with Google
- Token configuration through environment variables:
  ```properties
  jwt.token.secret=${JWT_TOKEN_SECRET}
  jwt.access.token.duration=${JWT_ACCESS_TOKEN_DURATION}
  jwt.refresh.token-expiration=${JWT_REFRESH_TOKEN_EXPIRATION}
  ```

### Cross-Component Communication

- Each module exposes REST APIs (see `*Controller` classes)
- Spring Boot starters for consistent configuration
- Common models shared through `commons` module
- Base packages scanned: `org.merra.*`

## Development Workflow

### Build and Run

1. Set required environment variables (see `application.properties`)
2. Ensure PostgreSQL is running on port 5070
3. Build all modules: `mvn clean install`
4. Run individual services or whole stack through Docker Compose

### Testing Patterns

- Unit tests with Spring Boot Test
- Integration tests with REST Assured
- Test classes follow `*Test.java` naming

### Common Files

- Key configs in `application.properties`
- Entity mapping with MapStruct (see `OrganizationMapper` for example)
- Liquibase changelogs in each module's resources

## Best Practices

- Follow existing package structure: `org.merra.[module].[component]`
- Use MapStruct for entity-DTO mapping
- Place database migrations in module-specific changelog files
- Validate requests using Spring Boot Validation annotations
- Handle errors consistently through global exception handlers

## Important URLs

- API documentation: `/api-docs`
- Health check: GET `/` returns "Merra accounting API is running..."
