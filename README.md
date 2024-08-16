# Bookstore Microservices Project

## Overview

This project is a cloud-native microservices application designed to adhere to the 12-Factor App principles. It consists of multiple services that communicate with each other via REST APIs and messaging queues. The services include:

- **config-service**: Centralized configuration management.
- **gateway-service**: API Gateway that routes requests to appropriate services.
- **registry-service**: Service discovery and registration.
- **category-service**: Manages product categories.
- **order-service**: Manages customer orders.
- **notification-service**: Handles notifications and integrates with RabbitMQ for messaging.

## 12-Factor Principles

This project adheres to several 12-Factor principles, which are key to building cloud-native and scalable applications. Below are the principles that are most relevant to this project:

1. **Codebase**: 
   - **One codebase tracked in revision control, many deploys**. All services are version-controlled and can be deployed independently.
   
2. **Dependencies**:
   - **Explicitly declare and isolate dependencies**. Each service has its own set of dependencies managed by Gradle/Maven, and isolated in its respective environment.
   
3. **Config**:
   - **Store config in the environment**. Configuration for all services is centralized in the `config-service`, which fetches configurations based on the environment.

4. **Backing Services**:
   - **Treat backing services as attached resources**. The `notification-service` uses RabbitMQ, which is treated as an external resource and is configurable via the `config-service`.

5. **Build, Release, Run**:
   - **Strictly separate build and run stages**. CI/CD pipelines are set up to ensure that the build, release, and run stages are distinct, promoting consistency across environments.

6. **Processes**:
   - **Execute the app as one or more stateless processes**. Each service is stateless and can be scaled horizontally by adding more instances. The state is persisted in databases or external services like RabbitMQ.

7. **Port Binding**:
   - **Export services via port binding**. Each service runs on its own port, and the `gateway-service` binds these services together by routing requests to the appropriate ports.

8. **Concurrency**:
   - **Scale out via the process model**. The application is designed to be scalable. Each microservice can be independently scaled based on demand.

9. **Disposability**:
   - **Maximize robustness with fast startup and graceful shutdown**. The services are designed to start up quickly and gracefully handle shutdowns, making them robust and resilient.

10. **Dev/Prod Parity**:
    - **Keep development, staging, and production as similar as possible**. The services are tested in environments that closely mirror production to ensure consistent behavior.

11. **Logs**:
    - **Treat logs as event streams**. Logs are collected and monitored centrally, allowing for easy access and analysis across all services.

12. **Admin Processes**:
    - **Run admin/management tasks as one-off processes**. Any administrative tasks can be executed as one-off processes in the respective services.

## Microservices Architecture

This project follows a microservices architecture, where each service is a self-contained unit responsible for a specific business capability. Services communicate via RESTful APIs or messaging queues, enabling loose coupling and independent scalability. 

![Screenshot (249)](https://github.com/user-attachments/assets/03d4a1cc-68f9-49be-99f4-5354e80f15f2)

Below is a brief overview of each service:

### 1. **config-service**
   - **Role**: Centralized configuration management.
   - **Responsibilities**: Provides configuration data to all services from a centralized repository.
   - **Technologies**: Spring Cloud Config.

### 2. **gateway-service**
   - **Role**: API Gateway.
   - **Responsibilities**: Routes incoming requests to the appropriate services, handles cross-cutting concerns like authentication and rate limiting.
   - **Technologies**: Spring Cloud Gateway, Resilience4j.

### 3. **registry-service**
   - **Role**: Service discovery.
   - **Responsibilities**: Registers and discovers services dynamically, ensuring that services can find each other without hardcoding locations.
   - **Technologies**: Spring Cloud Netflix Eureka.

### 4. **category-service**
   - **Role**: Manages product categories.
   - **Responsibilities**: Provides CRUD operations for product categories.
   - **Technologies**: Spring Boot, PostgreSQL.

### 5. **order-service**
   - **Role**: Manages customer orders.
   - **Responsibilities**: Handles order creation, updates, and queries.
   - **Technologies**: Spring Boot, PostgreSQL.

### 6. **notification-service**
   - **Role**: Manages notifications.
   - **Responsibilities**: Sends notifications based on events triggered by other services, integrates with RabbitMQ for messaging.
   - **Technologies**: Spring Boot, RabbitMQ, Spring Cloud Stream.

## RabbitMQ Integration

The `notification-service` uses RabbitMQ for messaging. It subscribes to events like `order-accepted` and `order-notified`, processes these events, and sends notifications accordingly.

- **Stream Bindings**:
  - The service is configured to consume and produce messages to RabbitMQ using Spring Cloud Stream. Below is a basic configuration:
  
  ```yaml
  spring:
    cloud:
      stream:
        bindings:
          notifyOrder-in-0:
            destination: order-notified
            group: notification-group
          acceptOrder-out-0:
            destination: order-accepted
  ```

## Running the Project

### Prerequisites

- Java 17+
- Docker
- Docker Compose
- RabbitMQ

### Steps to Run

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/victoradepoju/bookstore.git
   cd yourproject
   ```

2. **Start the Config, Registry, and Gateway Services**:

   ```bash
   cd config-service
   mvn spring-boot:run
   
   cd ../registry-service
   mvn spring-boot:run
   
   cd ../gateway-service
   mvn spring-boot:run
   ```

3. **Start the Other Services**:

   ```bash
   cd ../category-service
   mvn spring-boot:run
   
   cd ../order-service
   mvn spring-boot:run
   
   cd ../notification-service
   mvn spring-boot:run
   ```

4. **Access the Services**:
   - Config Service: `http://localhost:8200`
   - Gateway Service: `http://localhost:8500`
   - Registry Service: `http://localhost:8761`

### Using Docker Compose

You can also start all the services using Docker Compose:

```bash
docker-compose up --build
```

This will build and start all the services, including the necessary dependencies like RabbitMQ.

## Contribution

Feel free to contribute by submitting a pull request or reporting issues. For major changes, please discuss them first by opening an issue.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


![Screenshot (249)](https://github.com/user-attachments/assets/03d4a1cc-68f9-49be-99f4-5354e80f15f2)


![Screenshot (248)](https://github.com/user-attachments/assets/5138dc09-837f-490c-b938-c7e0f09d05a4)


![Screenshot (247)](https://github.com/user-attachments/assets/f1347ac2-d9ee-4c9a-ad31-31c7993bfd8a)
