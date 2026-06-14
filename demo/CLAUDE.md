# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew build

# Run the application
./gradlew bootRun

# Run all tests
./gradlew test

# Run a single test
./gradlew test --tests "com.example.demo.DemoApplicationTests.contextLoads"
```

On Windows use `gradlew.bat` instead of `./gradlew`.

## Stack

- **Language**: Kotlin 1.9.25, targeting Java 21
- **Framework**: Spring Boot 3.5.14
- **Persistence**: Spring Data JPA + Hibernate (DDL auto=update) against PostgreSQL
- **Security**: Spring Security (configured but not yet customized)
- **Validation**: Jakarta Bean Validation via `spring-boot-starter-validation`

## Database

The app connects to a local PostgreSQL instance. Database name: `finance_tracker`. Connection details are in `src/main/resources/application.properties`. A running PostgreSQL instance is required to start the app or run `@SpringBootTest` context tests.

## Project State

This is a freshly generated scaffold — only `DemoApplication.kt` and a context-load test exist. Domain entities, repositories, controllers, and security configuration are yet to be added.
