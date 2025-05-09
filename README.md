SiemensJavaInternship2025

Welcome to the SiemensJavaInternship2025 repository! This Spring Boot application demonstrates a full-featured CRUD REST API backed by an in-memory H2 database, complete with validation, logging, asynchronous processing, and robust error handling.

Table of Contents

Project Overview

Architecture & Design

Key Features

Technology Stack

Detailed Implementation

Entity Layer

Repository Layer

Service Layer

Controller Layer

Validation & Error Handling

Asynchronous Processing

Logging

Testing

Configuration

Running the Application

Project Overview
SiemensJavaInternship2025 is a sample Spring Boot application developed to showcase:

CRUD operations on a simple Item resource via REST endpoints.

Field validation using Jakarta Bean Validation.

Asynchronous batch processing of entities with thread safety.

In-memory H2 database for rapid prototyping.

Comprehensive logging for tracing and debugging.

Unit and integration tests to ensure code quality.

This repository mirrors a real-world microservice implementation, complete with best practices around thread safety, resource management, and error handling.

┌───────────────────┐       ┌───────────────────┐       ┌───────────────────┐
│    Controllers    │──────▶│     Services      │──────▶│   Repositories    │
│  (REST API Layer) │       │ (Business Logic)  │       │ (Data Access)     │
└───────────────────┘       └───────────────────┘       └───────────────────┘
Controller: Exposes HTTP endpoints under /api/items, handles validation and maps service responses to ResponseEntity.

Service: Implements business logic (CRUD, validation checks, batch processing), uses Spring’s @Async and a fixed thread pool for concurrency, ensures thread safety with synchronized collections and atomic counters.

Repository: Extends JpaRepository<Item,Long> for CRUD and provides a custom JPQL query (findAllIds()) to fetch only IDs for efficient batch processing.

Key Features
CRUD Endpoints

GET /api/items – List all items

POST /api/items – Create a new item

GET /api/items/{id} – Retrieve an item by ID

PUT /api/items/{id} – Update an existing item

DELETE /api/items/{id} – Delete an item

Validation

Annotation-driven (@NotBlank, @Pattern) on each field.

Custom regex for email format ensuring local@domain.tld compliance.

Asynchronous Batch Processing

GET /api/items/process triggers processItemsAsync().

Uses CompletableFuture with a dedicated ExecutorService.

Awaits all tasks via CompletableFuture.allOf(...) to guarantee completion before returning.

Thread-safe accumulation using Collections.synchronizedList and AtomicInteger.

Logging

SLF4J + Logback configuration.

Console and rolling file appenders for real-time and persisted logs.

MDC support (%X{userId}) to track context, with timestamps and thread names.

Error Handling

Validation errors return 400 Bad Request with field-level messages.

Non-existent resources return 404 Not Found.

IllegalArgumentException for invalid IDs yields clear error messages.

Testing

Unit tests for service methods (ID validation).

Bean validation tests to confirm constraint enforcement.

Technology Stack
Java 23

Spring Boot 3.3.11

Spring Data JPA

H2 Database (in-memory)

Jakarta Bean Validation (Hibernate Validator)

SLF4J + Logback for logging

JUnit 5 & TestNG for testing

Maven for build and dependency management


