# TournamentManagerPro

> A production-style backend REST API project built using Java 17, Spring Boot 3, Spring Security, JPA/Hibernate, and MySQL.

The application is designed to manage tournaments, teams, users, and registrations through secure REST APIs while following clean layered architecture principles used in real-world backend systems.

A backend REST API project built using Java 17, Spring Boot, Spring Security, and MySQL for managing tournaments, teams, and registrations.

The project follows a layered architecture approach and demonstrates backend development concepts such as authentication & authorization, REST API development, exception handling, database management, and unit testing.

---

# Quick Project Overview

## Features

* User Registration & Authentication
* Role-Based Access Control using Spring Security
* Tournament Management APIs
* Team Management APIs
* Registration Management APIs
* RESTful API Architecture
* Global Exception Handling
* Layered Architecture
* MySQL Database Integration

---

# Tech Stack

| Technology        | Purpose                        |
| ----------------- | ------------------------------ |
| Java 17           | Core Programming Language      |
| Spring Boot 3.5.4 | Backend Framework              |
| Spring Security   | Authentication & Authorization |
| Spring Data JPA   | Database Operations            |
| Hibernate         | ORM Framework                  |
| MySQL             | Relational Database            |
| Maven             | Dependency Management          |
| Lombok            | Boilerplate Code Reduction     |

---

# Project Architecture

The project follows a layered architecture pattern to maintain clean code structure and separation of concerns.

## Layers Used

### 1. Controller Layer

Handles HTTP requests and API endpoints.

Examples:

* TournamentController
* TeamController
* RegistrationController
* UserController

### 2. Service Layer

Contains all business logic and application operations.

Examples:

* TournamentService
* TeamService
* RegistrationService
* UserService

### 3. Repository Layer

Handles database interactions using Spring Data JPA.

Examples:

* TournamentRepository
* TeamRepository
* RegistrationRepository
* UserRepository

### 4. Entity Layer

Represents database tables using JPA entities.

Examples:

* Tournament
* Team
* Registration
* User
* Role

### 5. Exception Handling Layer

Global exception handling is implemented using @ControllerAdvice for better API error responses.

---

# Main Functionalities

## User Management

* User Registration
* Fetch Current User
* Fetch Users by Role
* Delete User

## Tournament Management

* Create Tournament
* Update Tournament
* Delete Tournament
* Fetch All Tournaments
* Fetch Tournament By ID
* Fetch User-Owned Tournaments

## Team Management

* Create Team
* Update Team
* Delete Team
* Fetch Teams
* Fetch Teams By Tournament

## Registration Management

* Register Team into Tournament
* Fetch Registration Details
* Delete Registration
* Fetch Registrations by Tournament
* Fetch Registrations by Team
* Count Tournament Registrations

---

# API Endpoints

## User APIs

| Method | Endpoint               | Description          |
| ------ | ---------------------- | -------------------- |
| POST   | /api/users/register    | Register new user    |
| GET    | /api/users             | Fetch all users      |
| GET    | /api/users/me          | Fetch logged-in user |
| GET    | /api/users/role/{role} | Fetch users by role  |
| DELETE | /api/users/{id}        | Delete user          |

---

## Tournament APIs

| Method | Endpoint              | Description                      |
| ------ | --------------------- | -------------------------------- |
| POST   | /api/tournaments      | Create tournament                |
| GET    | /api/tournaments      | Fetch all tournaments            |
| GET    | /api/tournaments/my   | Fetch current user's tournaments |
| GET    | /api/tournaments/{id} | Fetch tournament by ID           |
| PUT    | /api/tournaments/{id} | Update tournament                |
| DELETE | /api/tournaments/{id} | Delete tournament                |

---

## Team APIs

| Method | Endpoint                             | Description               |
| ------ | ------------------------------------ | ------------------------- |
| POST   | /api/teams                           | Create team               |
| GET    | /api/teams                           | Fetch all teams           |
| GET    | /api/teams/{id}                      | Fetch team by ID          |
| PUT    | /api/teams/{id}                      | Update team               |
| DELETE | /api/teams/{id}                      | Delete team               |
| GET    | /api/teams/tournament/{tournamentId} | Fetch teams by tournament |

---

## Registration APIs

| Method | Endpoint                                           | Description                    |
| ------ | -------------------------------------------------- | ------------------------------ |
| POST   | /api/registrations                                 | Register team                  |
| GET    | /api/registrations                                 | Fetch all registrations        |
| GET    | /api/registrations/{id}                            | Fetch registration by ID       |
| DELETE | /api/registrations/{id}                            | Delete registration            |
| GET    | /api/registrations/tournament/{tournamentId}       | Fetch tournament registrations |
| GET    | /api/registrations/team/{teamId}                   | Fetch team registrations       |
| GET    | /api/registrations/count/tournament/{tournamentId} | Count tournament registrations |

---

# Security Implementation

Spring Security is implemented in the project for authentication and authorization.

## Security Features

* Role-Based Access Control
* Protected API Endpoints
* Custom UserDetailsService
* Authentication Handling
* Secure API Access

The application uses different user roles to manage access permissions for different operations.

---

# Database Design

The application uses MySQL as the relational database.

## Main Tables

* users
* tournaments
* teams
* registrations
* roles

Relationships between entities are managed using JPA and Hibernate.

---

# Testing

Basic API testing and verification were performed using Postman.

The project structure is designed in a way that supports future automated testing implementation.

---

# Exception Handling

Global exception handling is implemented using:

* @ControllerAdvice
* Custom exception responses
* Proper HTTP status codes

This ensures better API response handling and cleaner error management.

---

# How to Run the Project

## Prerequisites

* Java 17
* MySQL
* Maven
* IntelliJ IDEA or any Java IDE

---

## Steps to Run

### 1. Clone Repository

```bash
git clone https://github.com/harshrandhir258/TournamentManagerPro.git
```

---

### 2. Configure Database

Create a MySQL database.

Example:

```sql
CREATE DATABASE tournamentmanager;
```

---

### 3. Configure application.properties

Create a file named:

```text
application.properties
```

inside:

```text
src/main/resources
```

Add your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tournamentmanager
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

---

### 4. Run Application

Run:

```bash
mvn spring-boot:run
```

or run the main application class directly from IntelliJ IDEA.

---

# Why This Project Stands Out

* Built using modern Java 17 and Spring Boot 3
* Implements layered architecture used in enterprise applications
* Includes Spring Security integration
* Uses RESTful API design principles
* Contains proper separation of concerns
* Includes exception handling and validation
* Demonstrates backend scalability concepts
* Follows clean project structure and Maven build management

---

# Internal Request Flow

The project follows the following backend request lifecycle:

```text
Client Request
      ↓
Controller Layer
      ↓
Service Layer
      ↓
Repository Layer
      ↓
MySQL Database
```

This architecture improves:

* Maintainability
* Scalability
* Testability
* Code readability
* Separation of concerns

---

# Authentication & Authorization Flow

Spring Security is integrated to secure API endpoints.

## Security Workflow

1. User sends request
2. Spring Security intercepts request
3. User authentication is validated
4. Role-based access is checked
5. API access is granted or denied

The project uses:

* CustomUserDetailsService
* Role-based authorization
* Protected endpoints
* Authentication manager configuration

---

# Project Highlights

* Clean Layered Architecture
* RESTful API Development
* Spring Security Integration
* Database Relationship Handling
* Unit Testing
* Exception Handling
* Professional Backend Structure

---

# Future Enhancements

* Frontend Integration using React
* JWT Authentication
* Swagger/OpenAPI Documentation
* Responsive User Interface
* Cloud Deployment

---

# Learning Outcomes

This project helped in understanding:

* Spring Boot Development
* REST API Design
* Spring Security
* Database Integration using JPA
* Layered Backend Architecture
* Exception Handling
* Unit Testing
* Maven Project Management
* Git & GitHub Workflow

---

# Author

Harsh Randhir

Backend Developer | Java & Spring Boot Enthusiast
