# Kost API

Backend REST API for a kost management system built with Spring Boot.

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- JWT Authentication
- Maven
- JUnit 5
- MockMvc

---

## Features

### Authentication

- Register
- Login
- Get current user (`/me`)

### Kost

- Owner can create kost
- Owner can update own kost
- Owner can delete own kost
- Owner can view own kost list
- Public search kost
- Public view kost detail

### Availability Request

- Availability request deducts 5 credits from the user's credit
- Owner cannot request availability
- Request is rejected when credit is insufficient

### Monthly Credit Recharge

- Scheduled monthly credit reset
- Owner credit is always 0
- Regular credit is reset to 20
- Premium credit is reset to 40

---

## Requirements

- Java 21
- Maven 3.9+
- PostgreSQL

---

## Configuration

Create your own configuration file.

Example:

`application-local.yaml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/kost_db
    username: postgres
    password: password

jwt:
  secret: your-secret-key
  expiration: 86400000
```

The repository only contains the default `application.yaml`.

Local configuration files such as:

- `application-local.yaml`
- `application-test.yaml`

are ignored by Git.

---

## Installation

Clone the repository

```bash
git clone <repository-url>
cd kost-api
```

Build the project and download dependencies

```bash
./mvnw clean install
```

or

```bash
mvn clean install
```

Create your local configuration file

```
src/main/resources/application-local.yaml
```

Example:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/kost_db
    username: postgres
    password: password

jwt:
  secret: your-secret-key
  expiration: 86400000
```

Create the PostgreSQL database

```
kost_db
```

Flyway migrations will run automatically when the application starts.

Start the application

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

API available at

```
http://localhost:8080
```

---

## Database

Create a PostgreSQL database.

```
kost_db
```

Flyway migrations will run automatically on application startup.

---

## Running

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

---

## Running Tests

```bash
./mvnw clean verify
```

or

```bash
./mvnw test
```

---

## API

### Authentication

| Method | Endpoint |
|---------|----------|
| POST | `/api/auth/register` |
| POST | `/api/auth/login` |
| GET | `/api/auth/me` |

### Kost

| Method | Endpoint |
|---------|----------|
| POST | `/api/kosts` |
| GET | `/api/kosts` |
| GET | `/api/kosts/{id}` |
| GET | `/api/kosts/owner` |
| PUT | `/api/kosts/{id}` |
| DELETE | `/api/kosts/{id}` |

### Availability

| Method | Endpoint |
|---------|----------|
| POST | `/api/availability` |

---

## Monthly Credit Scheduler

Credits are automatically reset every month by Spring Scheduler.

| Role | Monthly Credit |
|------|---------------:|
| OWNER | 0 |
| REGULAR | 20 |
| PREMIUM | 40 |

Scheduler implementation:

```
src/main/java/com/kost/kostapi/scheduler/CreditScheduler.java
```

Business logic:

```
src/main/java/com/kost/kostapi/service/CreditService.java
```

Cron expression:

```java
@Scheduled(cron = "0 0 0 1 * *")
```

Runs at 00:00 on the first day of every month.

To enable scheduling, add:

```java
@EnableScheduling
```

to the Spring Boot application class.

---

## Testing

Integration tests cover:

- Authentication
- Kost CRUD
- Authorization
- Availability request
- Credit deduction
- Monthly credit recharge
- Validation
- Error handling

Unit tests cover:

- CreditService
- CreditScheduler

Total tests:

```
30 tests
```

---

## Project Structure

```
kost-api/
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── kost
│   │   │           └── kostapi
│   │   │               ├── controller
│   │   │               ├── service
│   │   │               ├── scheduler
│   │   │               ├── repository
│   │   │               ├── entity
│   │   │               ├── dto
│   │   │               ├── mapper
│   │   │               ├── specification
│   │   │               ├── security
│   │   │               ├── config
│   │   │               ├── exception
│   │   │               └── KostApiApplication.java
│   │   └── resources
│   │       ├── db
│   │       │   └── migration
│   │       └── application.yaml
│   │
│   └── test
│       └── java
│           └── com
│               └── kost
│                   └── kostapi
│                       ├── util
│                       │   └── TestDataFactory.java
│                       ├── BaseIntegrationTest.java
│                       ├── AuthControllerTest.java
│                       ├── KostControllerTest.java
│                       ├── AvailabilityControllerTest.java
│                       ├── CreditServiceTest.java
│                       └── CreditSchedulerTest.java
│
├── pom.xml
└── README.md
```