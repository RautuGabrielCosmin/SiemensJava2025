# SiemensJavaInternship2025

A Spring Boot–powered REST API demonstrating a simple “Item” management microservice, built as part of the Siemens Java Internship 2025. This project showcases end-to-end CRUD operations, field validation, asynchronous processing, logging, and in-memory persistence.

## 🚀 Features

- **CRUD Endpoints**  
  - `GET /api/items` — list all items  
  - `POST /api/items` — create a new item (with bean-validation)  
  - `GET /api/items/{id}` — fetch a single item by ID  
  - `PUT /api/items/{id}` — update an existing item  
  - `DELETE /api/items/{id}` — remove an item  

- **Input Validation**  
  - `@NotBlank` on all fields  
  - Custom email format check via `@Pattern`

- **Asynchronous Batch Processing**  
  - `GET /api/items/process` triggers background processing of every item  
  - Uses Spring’s `@Async`, `CompletableFuture`, a fixed 10-thread pool, and thread-safe collections

- **Logging & Monitoring**  
  - SLF4J + Logback configuration with console and rolling-file appenders  
  - MDC support for contextual tracing

- **In-Memory H2 Database**  
  - Rapid prototyping with `spring.jpa.hibernate.ddl-auto=update`  
  - H2 console enabled at `/h2-console`

- **Automated Tests**  
  - Unit tests for ID validation and bean constraints (JUnit 5 & TestNG)

## 🏗️ Tech Stack

- Java 23 & Spring Boot 3.3.11  
- Spring Data JPA (Hibernate)  
- Jakarta Bean Validation (Hibernate Validator)  
- H2 Database  
- SLF4J / Logback  
- JUnit 5 & TestNG  
- Maven

## 📂 Project Structure
src/
├─ main/
│ ├─ java/com/siemens/internship/
│ │ ├─ InternshipApplication.java // bootstrap
│ │ ├─ entity/Item.java // JPA entity + validation
│ │ ├─ repository/ItemRepository.java
│ │ ├─ service/ItemService.java // business logic + async
│ │ └─ web/ItemController.java // REST API
│ └─ resources/
│ ├─ application.properties // H2 & JPA settings
│ └─ logback.xml // logging config
└─ test/
└─ InternshipApplicationTests.java // unit & validation tests


## ▶️ Getting Started

1. **Clone**  
   ```bash
   git clone https://github.com/yourusername/SiemensJavaInternship2025.git
   cd SiemensJavaInternship2025
     
2. Build & Run
   mvn clean install
   mvn spring-boot:run

3. Explore:
   API: http://localhost:8081/api/items
   H2 console: http://localhost:8081/h2-console
