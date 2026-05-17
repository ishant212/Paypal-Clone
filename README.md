# 💳 PayPal Clone — Microservices Backend

> A scalable, production-inspired payment platform built with Spring Boot microservices architecture.

![Java](https://img.shields.io/badge/Java-17+-red?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=flat-square)
![Spring Security](https://img.shields.io/badge/Spring_Security-configured-blue?style=flat-square)
![JWT](https://img.shields.io/badge/Auth-JWT-orange?style=flat-square)
![H2](https://img.shields.io/badge/Database-H2_In--Memory-yellow?style=flat-square)
![Maven](https://img.shields.io/badge/Build-Maven-orange?style=flat-square)
![Status](https://img.shields.io/badge/Status-Stage_2_Complete-purple?style=flat-square)

---

## 📌 Project Status

> **Stage 2 — JWT Authentication & Authorization (Complete)**
> This project is under active development. The README is updated alongside each development stage.

| Stage | Milestone | Status |
|-------|-----------|--------|
| 1 | User Service (CRUD + Security Setup) | ✅ Complete |
| 2 | JWT Authentication & Authorization | ✅ Complete |
| 3 | Transaction Service | 🔜 Upcoming |
| 4 | API Gateway + Service Discovery | 🔜 Upcoming |
| 5 | Wallet & Balance Management | 🔜 Upcoming |
| 6 | Notifications Service | 🔜 Upcoming |
| 7 | Docker & Kubernetes Deployment | 🔜 Upcoming |

---

## Table of Contents

1. [Overview](#1-overview)
2. [Current Microservices](#2-current-microservices)
3. [Architecture](#3-architecture)
4. [Tech Stack](#4-tech-stack)
5. [Folder Structure](#5-folder-structure)
6. [How to Run](#6-how-to-run)
7. [API Endpoints](#7-api-endpoints)
8. [Screenshots](#8-screenshots)
9. [Planned Microservices](#9-planned-microservices)
10. [Contributors](#10-contributors)

---

## 1. Overview

A backend system inspired by PayPal, designed to demonstrate real-world **microservices architecture** using Spring Boot. The platform handles user management, JWT-based authentication, and (soon) financial transactions — built with scalability, security, and clean design as core principles.

Each service is independently deployable, loosely coupled, and communicates over REST (with event-driven messaging planned via Kafka/RabbitMQ in later stages).

---

## 2. Current Microservices

### 👤 User Service *(Stage 1 & 2)*

Handles core user lifecycle management and authentication.

**Stage 1 Features:**
- Create a new user
- Fetch user by ID
- Fetch all users
- Update user details
- Custom exception handling for cleaner error responses
- Spring Security configuration (foundation for JWT)

**Stage 2 Features:**
- Signup API with BCrypt password hashing
- Login API with JWT token generation
- JWT request filter for stateless authentication
- Role-based claims inside JWT payload
- Spring Security context population per request
- Stateless session management

---

## 3. Architecture

### JWT Authentication Flow

```
Signup → Store User (BCrypt password)
Login  → Validate credentials → Generate JWT
       → Send JWT in Authorization header
       → JWTRequestFilter validates token
       → Populate SecurityContext
       → Access protected endpoints
```

### Layered Architecture (Per Service)

```
Client Request
     ↓
Controller Layer      → Handles HTTP requests & responses
     ↓
Service Layer         → Business logic
     ↓
Repository Layer      → Database operations (Spring Data JPA)
     ↓
Database (H2 / MySQL) → Persistence
```

### System Architecture *(Evolves Each Stage)*

```
[Clients]
    ↓
[API Gateway]             ← Stage 4
    ↓
┌─────────────────────────────────┐
│  User Service     ← Stage 1 ✅  │
│  Auth (JWT)       ← Stage 2 ✅  │
│  Transaction Svc  ← Stage 3     │
│  Wallet Service   ← Stage 5     │
│  Notification Svc ← Stage 6     │
└─────────────────────────────────┘
    ↓
[Message Broker - Kafka/RabbitMQ] ← Stage 6
```

---

## 4. Tech Stack

| Technology | Purpose | Status |
|------------|---------|--------|
| Java 17+ | Core language | ✅ Active |
| Spring Boot 3.x | Application framework | ✅ Active |
| Spring Data JPA | ORM & database operations | ✅ Active |
| Spring Security | Auth & authorization | ✅ Active |
| JJWT 0.12.x | JWT generation & validation | ✅ Active |
| H2 Database | In-memory DB (dev) | ✅ Active |
| BCrypt | Password hashing | ✅ Active |
| MySQL / PostgreSQL | Production DB | 🔜 Stage 3+ |
| Maven | Build tool | ✅ Active |
| API Gateway | Routing & load balancing | 🔜 Stage 4 |
| Eureka | Service discovery | 🔜 Stage 4 |
| Kafka / RabbitMQ | Async messaging | 🔜 Stage 6 |
| Docker | Containerization | 🔜 Stage 7 |
| Kubernetes | Orchestration | 🔜 Stage 7 |

---

## 5. Folder Structure

```
paypal-clone/
│
├── assets/                            # Screenshots
│
├── user-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/paypal/user_service/
│   │   │   │   ├── controller/        # AuthController, UserController
│   │   │   │   ├── dto/               # JwtResponse, LoginRequest, SignupRequest
│   │   │   │   ├── entity/            # User
│   │   │   │   ├── repository/        # UserRepository
│   │   │   │   ├── security/          # SecurityConfig
│   │   │   │   ├── service/           # UserService, UserServiceImpl
│   │   │   │   └── util/              # JWTUtil, JWTRequestFilter
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
└── README.md
```

> 📁 Additional service folders will be added as the project progresses through each stage.

---

## 6. How to Run

### Prerequisites

- Java 17+
- Maven 3.8+

### Step 1 — Clone Repository

```bash
git clone https://github.com/ishant212/paypal-clone
cd paypal-clone
```

### Step 2 — Navigate to User Service

```bash
cd user-service
```

### Step 3 — Build & Run

```bash
mvn spring-boot:run
```

### Step 4 — Access H2 Console *(Optional)*

```
URL:      http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (leave blank)
```

> ⚠️ H2 is an in-memory database used for development. Data resets on restart. MySQL/PostgreSQL will be integrated in a later stage.

---

## 7. API Endpoints

### Auth — Base URL: `/auth`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/auth/signup` | Register a new user | ❌ |
| `POST` | `/auth/login` | Login and receive JWT | ❌ |

### Sample Request — Signup

```json
POST /auth/signup
Content-Type: application/json

{
  "name": "Ishant Shekhar",
  "email": "ishant@example.com",
  "password": "securepassword"
}
```

### Sample Response — Signup

```
User Registration successful
```

### Sample Request — Login

```json
POST /auth/login
Content-Type: application/json

{
  "email": "ishant@example.com",
  "password": "securepassword"
}
```

### Sample Response — Login

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwic3ViIjoiaXNoYW50QGV4YW1wbGUuY29tIn0..."
}
```

> Use the returned token as `Authorization: Bearer <token>` in subsequent requests to protected endpoints.

---

### User Service — Base URL: `/api/users`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/users` | Create a new user | ❌ |
| `GET` | `/api/users/{id}` | Fetch user by ID | ✅ |
| `GET` | `/api/users` | Fetch all users | ✅ |

### Sample Request — Create User

```json
POST /api/users
Content-Type: application/json

{
  "name": "Ishant Shekhar",
  "email": "ishant@example.com",
  "password": "securepassword"
}
```

### Sample Response — Create User

```json
{
  "id": 1,
  "name": "Ishant Shekhar",
  "email": "ishant@example.com",
  "createdAt": "2025-01-01T10:00:00"
}
```

> 📋 Full API documentation (Swagger/OpenAPI) will be added in a future stage.

---

## 8. Screenshots

### 🟢 Signup — POST `/auth/signup`
![Signup](assets/user_signup.png)

### 🔵 Login — POST `/auth/login`
![Login](assets/user_login.png)

### 🟢 Create User — POST `/api/users`
![Create User](assets/create_user.png)

### 🔵 Fetch User by ID — GET `/api/users/{id}`
![Get User by ID](assets/get_user_by_id.png)

### 🟡 Fetch All Users — GET `/api/users`
![Get All Users](assets/get_all_users.png)

> 📸 Screenshots captured using Postman. More will be added with each new feature.

---

## 9. Planned Microservices

| Service | Responsibility |
|---------|---------------|
| Transaction Service | Send/receive money, transaction history |
| Wallet Service | Balance management, top-up |
| Notification Service | Email/SMS alerts via Kafka events |
| API Gateway | Centralized routing, rate limiting |

---  
> ⭐ This project is being built stage by stage as a portfolio demonstration of real-world microservices design. Star it to follow along!
