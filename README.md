# 💳 PayPal Clone — Microservices Backend

> A scalable, production-inspired payment platform built with Spring Boot microservices architecture.

![Java](https://img.shields.io/badge/Java-17+-red?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=flat-square)
![Spring Security](https://img.shields.io/badge/Spring_Security-configured-blue?style=flat-square)
![H2](https://img.shields.io/badge/Database-H2_In--Memory-yellow?style=flat-square)
![Maven](https://img.shields.io/badge/Build-Maven-orange?style=flat-square)
![Status](https://img.shields.io/badge/Status-Stage_1_Active-purple?style=flat-square)

---

## 📌 Project Status

> **Stage 1 — User Service (Complete)**
> This project is under active development. The README is updated alongside each development stage.

| Stage | Milestone | Status |
|-------|-----------|--------|
| 1 | User Service (CRUD + Security Setup) | ✅ Complete |
| 2 | JWT Authentication & Authorization | 🔜 Upcoming |
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

A backend system inspired by PayPal, designed to demonstrate real-world **microservices architecture** using Spring Boot. The platform handles user management, authentication, and (soon) financial transactions — built with scalability, security, and clean design as core principles.

Each service is independently deployable, loosely coupled, and communicates over REST (with event-driven messaging planned via Kafka/RabbitMQ in later stages).

---

## 2. Current Microservices

### 👤 User Service *(Stage 1)*

Handles core user lifecycle management.

**Features:**
- Create a new user
- Fetch user by ID
- Fetch all users
- Update user details
- Custom exception handling for cleaner error responses
- Spring Security configuration (foundation for JWT in Stage 2)

---

## 3. Architecture

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
│  Auth Service     ← Stage 2     │
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
| Spring Security | Auth foundation | ✅ Configured |
| H2 Database | In-memory DB (dev) | ✅ Active |
| MySQL / PostgreSQL | Production DB | 🔜 Stage 2+ |
| Maven | Build tool | ✅ Active |
| JWT | Token-based auth | 🔜 Stage 2 |
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
├── user-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/paypalclone/userservice/
│   │   │   │   ├── controller/        # REST controllers
│   │   │   │   ├── service/           # Business logic
│   │   │   │   ├── repository/        # JPA repositories
│   │   │   │   ├── model/             # Entity classes
│   │   │   │   ├── exception/         # Custom exceptions
│   │   │   │   └── config/            # Security config
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
git clone https://github.com/your-username/paypal-clone
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

> ⚠️ H2 is an in-memory database used for development. Data resets on restart. MySQL/PostgreSQL will be integrated in Stage 2.

---

## 7. API Endpoints

### User Service — Base URL: `/api/users`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/users` | Create a new user |
| `GET` | `/api/users/{id}` | Fetch user by ID |
| `GET` | `/api/users` | Fetch all users |
| `PUT` | `/api/users/{id}` | Update user details |

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

### Sample Response

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

### 🟢 Create User — POST `/api/users`
![Create User](assets/create_user.png)

### 🔵 Fetch User by ID — GET `/api/users/{id}`
![Get User by ID](assets/get_user_by_id.png)

### 🟡 Fetch All Users — GET `/api/users`
![Get All Users](assets/get_all_users.png)

### 🟠 Update User — PUT `/api/users/{id}`
![Update User](assets/update_user.png)

> 📸 Screenshots captured using Postman. More will be added with each new feature.

---

## 9. Planned Microservices

| Service | Responsibility |
|---------|---------------|
| Auth Service | JWT login, token refresh, logout |
| Transaction Service | Send/receive money, transaction history |
| Wallet Service | Balance management, top-up |
| Notification Service | Email/SMS alerts via Kafka events |
| API Gateway | Centralized routing, rate limiting |

---

## 10. Contributors

| Name | GitHub |
|------|--------|
| *(Your name here)* | [@yourusername](https://github.com/yourusername) |

---

> ⭐ This project is being built stage by stage as a portfolio demonstration of real-world microservices design. Star it to follow along!
