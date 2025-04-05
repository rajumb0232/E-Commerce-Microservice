# E-Commerce Microservices Application

This project is a simple e-commerce application built using a microservices architecture. It leverages Spring Boot and Spring Cloud to provide a scalable and maintainable solution. The application consists of multiple services, each responsible for a specific domain, and is organized as a polyrepo.

## Architecture Overview

The application is composed of the following microservices:

1. **Config Server**: 
   <br>Centralized configuration management for all services.
2. **Service Discovery**:
   <br>Service discovery for dynamic scaling and fault tolerance, set up using Consul.
3. **API Gateway**:
   <br>Entry point for all client requests, routing them to the appropriate services.
4. **User Service**:
   <br>Manages user registration, authentication, and profile management.
5. **Product Service**:
   <br>Handles product catalog management, including product details and inventory.
6. **Order Service**:
   <br>Manages order creation, processing, and tracking. (working)
7. **Rating and Review Service**:
   <br>Allows users to rate and review products. (working)

## Technologies Used

- Java 17
- Spring Boot 3.4.x
- Spring Cloud
- Spring Data JPA
- MySQL
- Redis
- Hashicorp Consul (Service Discovery)
- Spring Cloud Config
- Spring Cloud Gateway
- OpenFeign
- Lombok
- Maven

## Getting Started

### Prerequisites

- Java 17 (only)
- Maven (any latest version)
- MySQL database
- Redis
- Hashicorp Consul installed (for service discovery)

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/rajumb0232/E-Commerce-Microservice.git
