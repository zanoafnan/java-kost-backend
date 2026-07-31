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
- Credit deducted automatically

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

## Testing

Integration tests cover:

- Authentication
- Kost CRUD
- Authorization
- Availability request
- Credit deduction
- Validation
- Error handling

Total tests:

```
26 tests
```

---

## Project Structure

```
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
│                       └── AvailabilityControllerTest.java
│
├── pom.xml
└── README.md
```

```