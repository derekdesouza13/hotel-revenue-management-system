# Hotel Revenue Management System

A full-stack hotel revenue management platform built with **Java, Spring Boot, REST APIs, JPA/Hibernate, MySQL, React and Maven**.

The system manages hotel room inventory, bookings, occupancy, pricing rules and revenue analytics through a layered backend architecture and an interactive React dashboard.

---

## Overview

The Hotel Revenue Management System is designed to demonstrate the development of a data-driven hospitality application using enterprise-oriented software engineering practices.

The platform allows hotel staff and revenue managers to:

- Manage hotels and room inventory
- Manage guests and bookings
- Prevent overlapping room reservations
- Calculate hotel occupancy
- Calculate revenue and average daily rates
- Configure pricing rules
- Generate price recommendations based on occupancy and demand
- View revenue and occupancy analytics
- Access functionality based on user roles
- Interact with the system through REST APIs
- Persist application data in MySQL

The project follows a layered architecture separating controllers, business logic, data access and persistence.

---

## Key Features

### Hotel Management

- Create, update, retrieve and delete hotels
- Store hotel location and room inventory information
- Manage multiple hotels through a common application

### Room Inventory

- Create and manage hotel rooms
- Support multiple room types
- Track room availability and status
- Retrieve available rooms
- Associate rooms with individual hotels

### Booking Management

- Create and manage guest bookings
- Track check-in and check-out dates
- Calculate booking totals
- Cancel bookings
- Validate booking dates
- Prevent overlapping bookings for the same room

### Revenue Management

- Calculate hotel occupancy
- Calculate total revenue
- Calculate Average Daily Rate (ADR)
- Analyze room-type revenue
- Generate pricing recommendations
- Apply configurable occupancy-based pricing rules

### Dynamic Pricing

The pricing engine evaluates hotel conditions and calculates recommended room prices.

Example:

```text
Base Room Price: ₹5,000

Occupancy: 88%

Occupancy Adjustment: +25%

Recommended Price: ₹6,250
```

Pricing strategies can be extended to support:

- Occupancy-based pricing
- Seasonal pricing
- Weekend pricing
- High-demand pricing
- Lead-time adjustments

### Analytics Dashboard

The React dashboard provides:

- Total room count
- Occupied rooms
- Available rooms
- Occupancy percentage
- Total revenue
- Average Daily Rate
- Revenue trends
- Pricing recommendations

### Authentication & Authorization

The application supports role-based access control.

Roles include:

```text
ADMIN
REVENUE_MANAGER
STAFF
```

Different roles receive access to different application capabilities.

---

# Technology Stack

## Backend

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Security
- REST APIs
- Maven
- Bean Validation

## Database

- MySQL
- SQL
- JPA/Hibernate ORM

## Frontend

- React
- JavaScript
- HTML5
- CSS3

## Testing

- JUnit
- Mockito
- Spring Boot Test

## Development & Tools

- Git
- GitHub
- Postman
- Docker
- Docker Compose

---

# Architecture

The application follows a layered architecture.

```text
                    ┌─────────────────────┐
                    │    React Frontend   │
                    └──────────┬──────────┘
                               │
                               │ HTTP / JSON
                               ▼
                    ┌─────────────────────┐
                    │    REST Controllers │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │    Service Layer    │
                    │                     │
                    │ Business Logic      │
                    │ Pricing Engine      │
                    │ Booking Validation  │
                    │ Revenue Calculation │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │ Repository Layer    │
                    │ Spring Data JPA     │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │ Hibernate / JPA     │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │       MySQL         │
                    └─────────────────────┘
```

---

# Backend Architecture

The Spring Boot backend is organized into multiple layers.

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity
    ↓
MySQL
```

## Controller Layer

Responsible for:

- HTTP requests
- Request validation
- Response generation
- REST endpoint mapping

Example:

```text
RoomController
BookingController
PricingController
RevenueController
```

## Service Layer

Contains business logic.

Examples:

```text
RoomService
BookingService
PricingService
RevenueService
```

The service layer handles:

- Booking validation
- Double-booking prevention
- Occupancy calculations
- Revenue calculations
- Pricing recommendations

## Repository Layer

Uses Spring Data JPA repositories to communicate with the database.

Examples:

```text
RoomRepository
BookingRepository
HotelRepository
PricingRuleRepository
```

## Entity Layer

Represents persistent database entities.

Examples:

```text
Hotel
Room
Guest
Booking
PricingRule
User
```

---

# Object-Oriented Design

The project demonstrates core Object-Oriented Programming concepts.

## Encapsulation

Entity state is encapsulated within Java classes and exposed through controlled methods.

## Abstraction

Business concepts such as pricing are represented through interfaces.

Example:

```java
public interface PricingStrategy {

    BigDecimal calculatePrice(
        BigDecimal basePrice,
        double occupancy
    );
}
```

## Polymorphism

Different pricing strategies can implement the same interface.

```text
PricingStrategy
      │
      ├── OccupancyPricingStrategy
      ├── SeasonalPricingStrategy
      └── WeekendPricingStrategy
```

This allows the pricing engine to support new strategies without modifying existing business logic.

## Separation of Responsibilities

Controllers, services and repositories have clearly defined responsibilities, improving maintainability and testability.

---

# Database Design

The application uses MySQL for persistent storage.

## Main Tables

```text
hotels
rooms
guests
bookings
pricing_rules
users
```

### Hotel

```text
id
name
location
total_rooms
created_at
```

### Room

```text
id
hotel_id
room_number
room_type
base_price
status
```

### Guest

```text
id
name
email
phone
```

### Booking

```text
id
hotel_id
room_id
guest_id
check_in
check_out
booking_date
status
total_amount
```

### Pricing Rule

```text
id
hotel_id
rule_name
occupancy_threshold
adjustment_percentage
active
```

---

# Database Relationships

```text
Hotel
  │
  ├───────────────┐
  │               │
  ▼               ▼
Room           Booking
                  │
                  ├──── Guest
                  │
                  └──── Room

Hotel
  │
  └──── PricingRule
```

The database demonstrates:

- Primary keys
- Foreign keys
- One-to-many relationships
- Many-to-one relationships
- Constraints
- Indexing
- SQL aggregation
- Joins

---

# REST API

## Hotel APIs

```http
GET    /api/hotels
GET    /api/hotels/{id}
POST   /api/hotels
PUT    /api/hotels/{id}
DELETE /api/hotels/{id}
```

## Room APIs

```http
GET    /api/rooms
GET    /api/rooms/{id}
GET    /api/rooms/available
POST   /api/rooms
PUT    /api/rooms/{id}
DELETE /api/rooms/{id}
```

## Booking APIs

```http
GET    /api/bookings
GET    /api/bookings/{id}
POST   /api/bookings
PUT    /api/bookings/{id}/cancel
```

## Pricing APIs

```http
GET    /api/pricing/recommendation/{roomId}
GET    /api/pricing/rules
POST   /api/pricing/rules
PUT    /api/pricing/rules/{id}
DELETE /api/pricing/rules/{id}
```

## Revenue APIs

```http
GET /api/revenue/dashboard
GET /api/revenue/occupancy
GET /api/revenue/daily
GET /api/revenue/room-types
```

---

# Booking Validation

The application prevents double-booking by checking whether an existing booking overlaps with the requested reservation period.

Conceptually:

```text
Existing Booking:
June 10 → June 15

New Booking:
June 12 → June 17

Result:
REJECTED
```

While:

```text
Existing Booking:
June 10 → June 15

New Booking:
June 15 → June 18

Result:
VALID
```

This validation is implemented in the service layer before creating a booking.

---

# Revenue Metrics

The system calculates several hospitality-oriented metrics.

## Occupancy

```text
Occupancy =
Occupied Rooms / Total Available Rooms × 100
```

## Average Daily Rate

```text
ADR =
Room Revenue / Rooms Sold
```

## Revenue

```text
Revenue =
Sum of completed/confirmed booking values
```

Additional revenue metrics can be added as the system evolves.

---

# Pricing Engine

The pricing engine uses configurable business rules.

Example:

```text
Occupancy Range       Adjustment
----------------------------------
0–50%                    0%
50–70%                  +5%
70–85%                 +15%
85–95%                 +25%
95–100%                +40%
```

For example:

```text
Base Price = ₹5,000
Occupancy = 88%
Adjustment = +25%

Recommended Price = ₹6,250
```

The pricing architecture is designed so additional pricing strategies can be introduced without rewriting the core booking system.

---

# Testing

The backend includes unit and integration testing using JUnit and Mockito.

Example test cases:

```text
PricingServiceTest
├── shouldCalculateBasePrice()
├── shouldIncreasePriceForHighOccupancy()
└── shouldHandleLowOccupancy()

BookingServiceTest
├── shouldCreateBooking()
├── shouldRejectOverlappingBooking()
└── shouldRejectInvalidDates()

RevenueServiceTest
├── shouldCalculateOccupancy()
├── shouldCalculateRevenue()
└── shouldCalculateADR()
```

The goal is to test business-critical behavior rather than simply maximize code coverage.

---

# Project Structure

```text
hotel-revenue-management-system/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/derek/hotelrevenue/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── service/
│   │   │   │       ├── repository/
│   │   │   │       ├── model/
│   │   │   │       ├── dto/
│   │   │   │       ├── exception/
│   │   │   │       └── enums/
│   │   │   │
│   │   │   └── resources/
│   │   │
│   │   └── test/
│   │
│   ├── pom.xml
│   └── Dockerfile
│
├── frontend/
│
├── database/
│   ├── schema.sql
│   ├── seed.sql
│   └── queries.sql
│
├── docs/
│   ├── architecture.md
│   ├── database-schema.md
│   └── api-documentation.md
│
├── screenshots/
│
├── docker-compose.yml
├── .gitignore
└── README.md
```

---

# Running Locally

## Prerequisites

Install:

```text
Java
Maven
MySQL
Node.js
npm
Git
```

Optional:

```text
Docker
Docker Compose
```

---

## 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/hotel-revenue-management-system.git

cd hotel-revenue-management-system
```

---

## 2. Configure MySQL

Create the database:

```sql
CREATE DATABASE hotel_revenue_management;
```

Configure the Spring Boot application using:

```text
backend/src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_revenue_management
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

---

## 3. Run the backend

```bash
cd backend

mvn clean install

mvn spring-boot:run
```

Backend:

```text
http://localhost:8080
```

---

## 4. Run the frontend

```bash
cd frontend

npm install

npm run dev
```

Frontend:

```text
http://localhost:5173
```

---

# Running Tests

From the backend directory:

```bash
mvn test
```

To build the application:

```bash
mvn clean package
```

---

# API Testing

The REST APIs can be tested using:

- Postman
- Browser
- React frontend

A Postman collection will be provided in:

```text
docs/postman/
```

---

# Development Workflow

The project follows a feature-based Git workflow.

Example:

```text
main
 │
 ├── feature/hotel-management
 ├── feature/room-inventory
 ├── feature/booking-management
 ├── feature/pricing-engine
 ├── feature/revenue-analytics
 └── feature/authentication
```

Each feature is developed, tested and merged independently.

---

# Engineering Practices

The project focuses on:

- Object-Oriented Programming
- Layered architecture
- Separation of concerns
- REST API design
- Database normalization
- Exception handling
- Input validation
- Unit testing
- Dependency injection
- Git version control
- Maven-based builds
- API documentation
- Containerized development

---

# Future Enhancements

Potential future improvements include:

- Multi-tenant hotel organizations
- Advanced demand forecasting
- Seasonal demand models
- Machine-learning-based price recommendations
- Kafka-based event processing
- Redis caching
- Spring Batch revenue processing
- Advanced reporting
- Cloud deployment
- CI/CD pipeline
- Automated integration testing

---

# Project Objective

The objective of this project is to demonstrate practical experience building a production-style, data-driven web application using Java and relational database technologies.

The project emphasizes:

```text
Java
OOP
Spring Boot
REST
JPA
Hibernate
MySQL
SQL
React
JUnit
Mockito
Maven
Git
```

---

# Author

**Derek Dsouza**

B.Tech Computer Science Engineering

GitHub: https://github.com/derekdesouza13
