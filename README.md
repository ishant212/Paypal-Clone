# 💳 PayPal Clone — Microservices Backend

> A scalable, production-inspired payment platform built with Spring Boot microservices architecture.

![Java](https://img.shields.io/badge/Java-17+-red?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=flat-square)
![Spring Security](https://img.shields.io/badge/Spring_Security-configured-blue?style=flat-square)
![JWT](https://img.shields.io/badge/Auth-JWT-orange?style=flat-square)
![H2](https://img.shields.io/badge/Database-H2_In--Memory-yellow?style=flat-square)
![Maven](https://img.shields.io/badge/Build-Maven-orange?style=flat-square)
![Status](https://img.shields.io/badge/Status-Stage_5_In_Progress-purple?style=flat-square)
![Kafka](https://img.shields.io/badge/Messaging-Apache_Kafka-black?style=flat-square)
![Docker Compose](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square)
![Gateway](https://img.shields.io/badge/API_Gateway-Spring_Cloud_Gateway-6DB33F?style=flat-square)

---

## 📌 Project Status

> **Stage 5 — API Gateway (In Progress)**
> This project is under active development. The README is updated alongside each development stage.

| Stage | Milestone | Status |
|-------|-----------|--------|
| 1 | User Service (CRUD + Security Setup) | ✅ Complete |
| 2 | JWT Authentication & Authorization | ✅ Complete |
| 3 | Transaction Service | ✅ Complete |
| 4 | Notification Service + Reward Service (Kafka Event-Driven Messaging) | ✅ Complete |
| 5 | API Gateway (routing complete, rate limiting pending) | 🔄 In Progress |
| 6 | Wallet & Balance Management | 🔜 Upcoming |
| 7 | Docker & Kubernetes Deployment | 🔜 Upcoming |

---

## Table of Contents

1. [Overview](#1-overview)
2. [Current Microservices](#2-current-microservices)
3. [Kafka-Based Event-Driven Microservices](#3-kafka-based-event-driven-microservices-stage-4)
4. [API Gateway](#4-api-gateway-stage-5)
5. [Architecture](#5-architecture)
6. [Tech Stack](#6-tech-stack)
7. [Folder Structure](#7-folder-structure)
8. [How to Run](#8-how-to-run)
9. [API Endpoints](#9-api-endpoints)
10. [Screenshots](#10-screenshots)
11. [Planned Microservices](#11-planned-microservices)
12. [Contributors](#12-contributors)

---

## 1. Overview

A backend system inspired by PayPal, designed to demonstrate real-world **microservices architecture** using Spring Boot. The platform handles user management, JWT-based authentication, and financial transactions — built with scalability, security, and clean design as core principles.

Each service is independently deployable, loosely coupled, and communicates over REST for synchronous flows and **Apache Kafka** for asynchronous, event-driven communication (e.g. transaction → notification/reward events). All client traffic now enters through a central **API Gateway**, which routes requests to the appropriate downstream service.

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

### 💸 Transaction Service *(Stage 3 — Complete)*

Handles the creation and persistence of financial transactions between users.

**Stage 3 Features:**
- `Transaction` entity with fields: `id`, `senderId`, `receiverId`, `amount`, `timestamp`, `status`
- JPA annotations: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`
- Amount validation using `@Positive` with Spring Validation dependency
- Automatic `timestamp` population via `@PrePersist` using `LocalDateTime.now()`
- Default `status` set to `"PENDING"` on pre-persist
- `POST /api/transactions/create` endpoint — creates and persists a transaction, returns full transaction object
- Clean column naming (`sender_id`, `receiver_id`) to accurately reflect stored data
- Hibernate/H2 debugging — traced and fixed `NULL not allowed` constraint violations caused by incorrect getter/setter naming and JSON mapping issues

---

### 📣 Notification Service *(Stage 4 — Complete)*

Consumes transaction events asynchronously and generates notifications, decoupled from the Transaction Service via Kafka.

**Stage 4 Features:**
- Apache Kafka integration for event-driven communication between Transaction Service and Notification Service
- `KafkaEventProducer` in Transaction Service publishes transaction events to the `txn-initiated` topic after successful transaction creation
- `NotificationConsumer` in Notification Service listens to `txn-initiated` via `@KafkaListener` (`groupId: notification-group`)
- Transaction events are automatically deserialized into `Transaction` objects on consumption
- Notifications are generated and persisted using Spring Data JPA + H2 whenever a transaction completes
- Logging added for Kafka publish confirmation and consumption tracking
- Kafka and Zookeeper containerized via Docker Compose for local development

---

### 🎁 Reward Service *(Stage 4 — Complete)*

A second independent consumer of transaction events, implemented by following [this Spring Kafka microservices tutorial](https://www.youtube.com/watch?v=yDW3YvgfkoY&list=PLaihB5c0gLqZNjSIGHak3Fg_o-Sp1V_IU&index=8&t=6s). Awards a reward record for every completed transaction, fully decoupled from both the Transaction and Notification services.

**Stage 4 Features:**
- `RewardServiceApplication` runs as its own Spring Boot service, consuming from the same `txn-initiated` Kafka topic
- Dedicated `@KafkaListener` (separate consumer group) deserializes incoming `Transaction` events
- `Reward` entity persisted via Spring Data JPA + H2 on successful event consumption
- Verified end-to-end: a transaction created via Postman triggers Kafka delivery, with service logs confirming `Reward saved: com.paypal.reward_service.entity.Reward@...`
- Demonstrates Kafka's pub-sub model — both Notification Service and Reward Service independently consume the same event stream without coupling to each other

---

### 🚪 API Gateway *(Stage 5 — In Progress)*

A single entry point that routes all client requests to the appropriate downstream microservice, built by following [this Spring Cloud Gateway tutorial](https://www.youtube.com/watch?v=e02QT3UGsHE&list=PLaihB5c0gLqZNjSIGHak3Fg_o-Sp1V_IU&index=10&t=1534s).

**Stage 5 Features (completed so far):**
- Spring Cloud Gateway configured as the single entry point for all client traffic
- Route definitions mapping incoming paths to User Service, Transaction Service, Notification Service, and Reward Service
- Clients now hit the Gateway port instead of calling each service directly
- Verified routing works end-to-end across all downstream services
- **Centralized JWT authentication** — `JwtAuthFilter` validates the token on every incoming request *before* it's routed downstream, with `JwtUtil` handling token parsing/validation logic
- Unauthenticated or invalid-token requests are rejected at the Gateway, so downstream services no longer need to repeat JWT checks

**Pending:**
- Rate limiting (not yet implemented — planned as the next sub-task of Stage 5)

---

## 3. Kafka-Based Event-Driven Microservices *(Stage 4)*

Implemented asynchronous, pub-sub communication from the Transaction Service to two independent downstream consumers — Notification Service and Reward Service — using Apache Kafka.

### Features

- Transaction Service publishes transaction events to a single Kafka topic (`txn-initiated`)
- **Notification Service** consumes events via `@KafkaListener` (`notification-group`) and persists a notification record
- **Reward Service** independently consumes the *same* topic via its own consumer group and persists a reward record
- Transaction details are automatically deserialized into a `Transaction` object on each consumer
- Decoupled microservice communication using event-driven architecture — neither consumer is aware of the other

### Components Added

**Transaction Service**
- Configured Kafka Producer using Spring Kafka
- Created `KafkaEventProducer` to publish transaction events
- Published transaction details after successful transaction creation
- Added logging for Kafka event publishing and delivery confirmation

**Notification Service**
- Configured Kafka Consumer using Spring Kafka
- Created `NotificationConsumer` with `@KafkaListener`
- Consumes events from the `txn-initiated` topic
- Generates notification messages automatically
- Persists notifications using Spring Data JPA and H2 Database

**Reward Service**
- Configured as an independent Spring Boot microservice with its own Kafka consumer
- Consumes the same `txn-initiated` topic with a dedicated consumer group
- Persists a `Reward` entity for every transaction event received
- Built by following an external Spring Kafka microservices tutorial, adapted to this project's `Transaction` event schema

### Kafka Configuration

**Producer**
```java
kafkaTemplate.send("txn-initiated", transaction);
```

**Consumer**
```java
@KafkaListener(
    topics = "txn-initiated",
    groupId = "notification-group"
)
public void consumeTransaction(Transaction transaction)
```

### Event Flow

```
Client Request
      ↓
Transaction Service
      ↓
Save Transaction
      ↓
Publish Event to Kafka Topic (txn-initiated)
      ↓
Kafka Broker
      ↓
      ├──→ Notification Service Consumer → Generate Notification → Save Notification
      │
      └──→ Reward Service Consumer       → Generate Reward       → Save Reward
```

### Docker Setup

Kafka and Zookeeper were containerized using Docker Compose.

```yaml
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.1

  kafka:
    image: confluentinc/cp-kafka:7.4.1
```

Started using:

```bash
docker compose up -d
```

### Verification

**Transaction Creation**

A transaction was successfully created through the Transaction Service API.

Example request:
```json
{
  "senderId": 3,
  "receiverId": 2,
  "amount": 25000.125
}
```

Example response:
```json
{
  "id": 1,
  "senderId": 3,
  "receiverId": 2,
  "amount": 25000.125,
  "timestamp": "2026-06-26T23:31:06.6679027",
  "status": "SUCCESS"
}
```

**Kafka Event Processing — Notification Service**

Logs confirm:
- Transaction saved successfully
- Event published to Kafka
- Notification Service consumed the event
- Notification persisted in the database

Example logs:
```text
Kafka message sent successfully!
Received transaction: Transaction(id=4,...)
Notification saved successfully
```

**Kafka Event Processing — Reward Service**

The same transaction event was independently consumed by the Reward Service, confirming Kafka's pub-sub fan-out across multiple consumer groups.

Example log:
```text
Reward saved: com.paypal.reward_service.entity.Reward@4271a667
```

---

## 4. API Gateway *(Stage 5)*

A centralized **Spring Cloud Gateway** service was introduced as the single entry point for all client requests, built by following [this tutorial](https://www.youtube.com/watch?v=e02QT3UGsHE&list=PLaihB5c0gLqZNjSIGHak3Fg_o-Sp1V_IU&index=10&t=1534s).

### Features

- All incoming client requests now route through the Gateway instead of hitting individual services directly
- Route definitions map URL paths to the correct downstream microservice (User, Transaction, Notification, Reward)
- **Centralized JWT authentication** — `JwtAuthFilter` intercepts every request at the Gateway and validates the JWT via `JwtUtil` *before* forwarding it downstream
- Requests with a missing or invalid token are rejected at the Gateway itself, so downstream services no longer duplicate auth logic
- Centralizes the entry point, paving the way for cross-cutting concerns like rate limiting and logging in one place
- ⏳ **Rate limiting not yet implemented** — planned as the next step for this stage

### Gateway-Level JWT Authentication

```
Incoming Request
      ↓
JwtAuthFilter (Gateway)
      ↓
Extract token from Authorization header
      ↓
JwtUtil → Validate signature & expiry
      ↓
   ┌──────────────┴──────────────┐
   ↓                              ↓
Valid token                  Invalid / missing token
   ↓                              ↓
Forward to downstream         401 Unauthorized
service via route             (request blocked at Gateway)
```

Components:
- `filters/JwtAuthFilter` — Spring Cloud Gateway global/route filter that runs on every request
- `util/JwtUtil` — shared token parsing & validation logic (signature check, expiry check)

### Routing Flow

```
Client Request
      ↓
API Gateway (single entry point)
      ↓
JwtAuthFilter → validate token (see below)
      ↓ (valid)
      ├──→ /api/users/**         → User Service
      ├──→ /auth/**              → User Service (Auth, unauthenticated)
      ├──→ /api/transactions/**  → Transaction Service
      ├──→ Notification Service  (internal, Kafka-driven)
      └──→ Reward Service        (internal, Kafka-driven)
```

> Note: `/auth/signup` and `/auth/login` are excluded from JWT validation at the Gateway, since users don't have a token yet at that point. Notification Service and Reward Service are Kafka consumers with no public REST endpoints, so they aren't routed through the Gateway directly — only request/response (synchronous) services are.

### What's Next for Stage 5

- [ ] Add rate limiting (e.g. Redis-backed `RequestRateLimiter` or Resilience4j)
- [ ] Add Eureka-based service discovery so routes don't need hardcoded service URLs

---

## 5. Architecture

### JWT Authentication Flow

```
Signup → Store User (BCrypt password)
Login  → Validate credentials → Generate JWT
       → Send JWT in Authorization header
       → JWTRequestFilter validates token
       → Populate SecurityContext
       → Access protected endpoints
```

### Transaction Flow *(Stage 3)*

```
POST /api/transactions/create
       ↓
TransactionController
       ↓
TransactionService       → Validates input (amount > 0)
       ↓
@PrePersist              → Sets timestamp = now(), status = "PENDING"
       ↓
TransactionRepository    → Persists to H2 (MySQL in later stage)
       ↓
Response: { id, senderId, receiverId, amount, timestamp, status }
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
[API Gateway]             ← Stage 5 🔄 (routing ✅ / rate limiting ⏳)
    ↓
┌─────────────────────────────────┐
│  User Service     ← Stage 1 ✅  │
│  Auth (JWT)       ← Stage 2 ✅  │
│  Transaction Svc  ← Stage 3 ✅  │
│  Notification Svc ← Stage 4 ✅  │
│  Reward Svc       ← Stage 4 ✅  │
│  Wallet Service   ← Stage 6     │
└─────────────────────────────────┘
    ↓
[Message Broker - Apache Kafka] ← Stage 4 ✅
```

---

## 6. Tech Stack

| Technology | Purpose | Status |
|------------|---------|--------|
| Java 17+ | Core language | ✅ Active |
| Spring Boot 3.x | Application framework | ✅ Active |
| Spring Data JPA | ORM & database operations | ✅ Active |
| Spring Security | Auth & authorization | ✅ Active |
| JJWT 0.12.x | JWT generation & validation | ✅ Active |
| Spring Validation | Request validation (`@Positive`, etc.) | ✅ Active |
| Spring Kafka | Event-driven messaging (producer/consumer) — used by Notification Service & Reward Service | ✅ Active |
| Apache Kafka | Async message broker | ✅ Active |
| Zookeeper | Kafka cluster coordination | ✅ Active |
| Spring Cloud Gateway | API Gateway — centralized routing + JWT auth filter | ✅ Active |
| Docker Compose | Local Kafka/Zookeeper orchestration | ✅ Active |
| H2 Database | In-memory DB (dev) | ✅ Active |
| BCrypt | Password hashing | ✅ Active |
| MySQL / PostgreSQL | Production DB | 🔜 Upcoming |
| Maven | Build tool | ✅ Active |
| Rate Limiting (Redis / Resilience4j) | Gateway request throttling | 🔜 Stage 5 (pending) |
| Eureka | Service discovery | 🔜 Stage 5 |
| Kubernetes | Orchestration | 🔜 Stage 7 |

---

## 7. Folder Structure

```
paypal-clone/
│
├── assets/                            # Screenshots
│
├── docker-compose.yml                 # Kafka + Zookeeper setup ← Stage 4
│
├── api-gateway/                       ← Stage 5
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/paypal/api_gateway/
│   │   │   │   ├── filters/           # JwtAuthFilter
│   │   │   │   └── util/              # JwtUtil
│   │   │   └── resources/
│   │   │       └── application.yml    # Route definitions
│   │   └── test/
│   └── pom.xml
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
├── transaction-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/paypal/transaction_service/
│   │   │   │   ├── controller/        # TransactionController
│   │   │   │   ├── entity/            # Transaction
│   │   │   │   ├── kafka/             # KafkaEventProducer ← Stage 4
│   │   │   │   ├── repository/        # TransactionRepository
│   │   │   │   └── service/           # TransactionService, TransactionServiceImpl
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
├── notification-service/              ← Stage 4
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/paypal/notification_service/
│   │   │   │   ├── consumer/          # NotificationConsumer
│   │   │   │   ├── entity/            # Notification
│   │   │   │   ├── repository/        # NotificationRepository
│   │   │   │   └── service/           # NotificationService, NotificationServiceImpl
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
├── reward-service/                    ← Stage 4
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/paypal/reward_service/
│   │   │   │   ├── consumer/          # RewardConsumer
│   │   │   │   ├── entity/            # Reward
│   │   │   │   ├── repository/        # RewardRepository
│   │   │   │   └── service/           # RewardService, RewardServiceImpl
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
└── README.md
```

> 📁 Additional service folders will be added as the project progresses through each stage.

---

## 8. How to Run

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose (for Kafka + Zookeeper)

### Step 1 — Clone Repository

```bash
git clone https://github.com/ishant212/paypal-clone
cd paypal-clone
```

### Step 2 — Start Kafka & Zookeeper

```bash
docker compose up -d
```

### Step 3 — Navigate to a Service

```bash
cd api-gateway
# or
cd user-service
# or
cd transaction-service
# or
cd notification-service
# or
cd reward-service
```

### Step 4 — Build & Run

```bash
mvn spring-boot:run
```

> 💡 Start the downstream services first, then the **api-gateway** last, so its routes have something to forward to. Once running, send requests to the Gateway's port instead of each service's individual port.

### Step 5 — Access H2 Console *(Optional)*

```
URL:      http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (leave blank)
```

> ⚠️ H2 is an in-memory database used for development. Data resets on restart. MySQL/PostgreSQL will be integrated in a later stage.

---

## 9. API Endpoints

> 🚪 **All endpoints below are now reachable through the API Gateway**, which routes to the underlying service. The Gateway itself enforces JWT authentication via `JwtAuthFilter` — requests to protected routes without a valid token are rejected at the Gateway before reaching the service. Direct service ports still work too, but the Gateway is the intended single entry point going forward.

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

---

### Transaction Service — Base URL: `/api/transactions`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/transactions/create` | Create a new transaction (publishes Kafka event on success) | ✅ |

### Sample Request — Create Transaction

```json
POST /api/transactions/create
Content-Type: application/json

{
  "senderId": 1,
  "receiverId": 2,
  "amount": 140.12
}
```

### Sample Response — Create Transaction

```json
{
  "id": 1,
  "senderId": 1,
  "receiverId": 2,
  "amount": 140.12,
  "timestamp": "2026-05-21T09:43:31.8732542",
  "status": "SUCCESS"
}
```

> 📋 Full API documentation (Swagger/OpenAPI) will be added in a future stage.

---

### Notification Service *(Stage 4)*

The Notification Service has no public REST endpoints yet — it operates purely as a **Kafka consumer**, listening on the `txn-initiated` topic and persisting a notification record for each transaction event it receives. REST endpoints to fetch notification history are planned for a future stage.

---

### Reward Service *(Stage 4)*

Like the Notification Service, the Reward Service has no public REST endpoints — it runs as a standalone **Kafka consumer** on the `txn-initiated` topic, persisting a `Reward` record for every transaction it consumes. REST endpoints to fetch reward history are planned for a future stage.

---

## 10. Screenshots

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

### 🟢 Create Transaction — POST `/api/transactions/create`
![Create Transaction](assets/transaction-creation-postman.png)

### 📨 Kafka Event Processing — Transaction → Notification
![Kafka Notification Confirmation](assets/kafka-notification-confirmation.png)

### 🎁 Kafka Event Processing — Transaction → Reward
![Kafka Reward Confirmation](assets/kafka-reward-confirmation.png)

> 📸 Screenshots captured using Postman and IDE run logs. More will be added with each new feature.

---

## 11. Planned Microservices

| Service / Feature | Responsibility |
|---------|---------------|
| API Gateway — Rate Limiting | Throttle requests per client at the Gateway layer (pending for Stage 5) |
| Wallet Service | Balance management, top-up |
| Service Discovery (Eureka) | Dynamic service registration & lookup |

---  
> ⭐ This project is being built stage by stage as a portfolio demonstration of real-world microservices design. Star it to follow along!
