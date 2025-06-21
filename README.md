# 🛒 E-Commerce Microservices Application

This project is a simple e-commerce application built using a microservices architecture. It leverages Spring Boot and Spring Cloud to provide a scalable and maintainable solution. The application consists of multiple services, each responsible for a specific domain, and is organized as a polyrepo.

## 🧱 Services

- **Config Server**: Centralized Spring Cloud Config
- **Service Discovery**: Powered by Consul
- **API Gateway**: Single entry point via Spring Cloud Gateway
- **User Service**: Auth + user profiles
- **Product Service**: Product catalog + inventory
- **Order Service**: Order processing + tracking
- **Rating & Review Service**: (Coming soon)

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3.4.+
- Spring Cloud
- Spring Data JPA
- MySQL
- Redis
- OpenFeign
- Maven

## ⚙️ Getting Started

### Prerequisites

- Java 17
- Maven
- MySQL database
- Redis
- Hashicorp Consul installed (for service discovery)

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/rajumb0232/E-Commerce-Microservice.git
