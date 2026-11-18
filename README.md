# <img src="./src/assets/images/jasmine-tea.jpg" alt="FloraCafe Logo" width="40" align="center" /> FloraCafe – Backend




<h3 align="center">Spring Boot REST API for Café Management & Online Ordering</h3>



<p align="center">

A secure and modular backend application responsible for authentication, menu management, order processing, customer management, and café administration.

</p>

<p align="center">
<img src="https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk" alt="Java" />
<img src="https://img.shields.io/badge/Spring%20Boot-Backend-6DB33F?logo=springboot" alt="Spring Boot" />
<img src="https://img.shields.io/badge/API-REST-blue" alt="REST API" />
<img src="https://img.shields.io/badge/Security-JWT-green" alt="JWT" />
</p>



---



## 📌 Table of Contents

- [Project Overview](#project-overview)

- [Key Features](#key-features)

- [System Architecture](#system-architecture)

- [Technology Stack](#technology-stack)

- [Prerequisites](#prerequisites)

- [Getting Started](#getting-started)

- [Environment Configuration](#environment-configuration)

- [Running the Application](#running-the-application)

- [API Documentation](#api-documentation)

- [Authentication and Security](#authentication-and-security)

- [Database and Data Initialization](#database-and-data-initialization)

- [Exception Handling](#exception-handling)

- [Testing](#testing)

- [Deployment](#deployment)

- [Best Practices](#best-practices)

- [Troubleshooting](#troubleshooting)

- [Future Improvements](#future-improvements)

- [Author](#author)

- [License](#license)



---



## 📖 Project Overview



FloraCafe Backend is the server-side component of the FloraCafe full-stack café management and online ordering platform.



It provides RESTful APIs that allow the frontend application to communicate with the application's business logic and persistent data layer.



The backend is developed using Java and Spring Boot and follows a layered architecture that separates controllers, services, repositories, models, DTOs, and security-related components.



### 🎯 Main Objectives

* Provide secure customer authentication and registration.

* Manage customer accounts and administrative access.

* Serve dynamic café menu information.

* Handle customer orders and order status updates.

* Manage shop configuration and operating status.

* Provide administrative management operations.

* Centralize business logic and validation.

* Protect application resources using JWT-based authentication.



### 💡 Core Value Proposition

FloraCafe Backend provides a centralized, modular, and maintainable API foundation that supports customer-facing ordering functionality and administrative café operations.



---



## ✨ Key Features



### 🔐 Authentication and Security

* User registration & login.

* Password hashing support.

* JWT authentication service & filter.

* Custom user details service.

* Role-based authorization configuration.

* Centralized Spring Security configuration.



### 🍽️ Menu Management

* Retrieve menu items.

* Manage menu-related data.

* Menu item model and repository.

* Menu service layer.

* Initial menu data loading.



### 📦 Order Management

* Create customer orders.

* Retrieve order information.

* Update order information.

* Maintain order statuses.

* Store order items.

* Administrative order management.



### 🏪 Shop Management

* Retrieve shop settings.

* Update shop configuration.

* Maintain shop operating information.

* Support shop availability-related functionality.



### 👨‍💼 Administration

* Administrative authentication.

* Customer management.

* Order management.

* Menu-related administrative operations.

* Shop settings management.

* Sales-related data access.



### ⚡ Additional Capabilities

* Global exception handling.

* Request and response DTOs.

* WebSocket configuration.

* Centralized business services.

* Initial menu data configuration.



---



## 🏗️ System Architecture



The backend follows a layered architecture inspired by Spring Boot enterprise application design.



### High-Level Architecture

```text

                  REACT FRONTEND

                         |

                         v

                   HTTP / REST

                         |

                         v

               SPRING SECURITY

                         |

                   JWT FILTER

                         |

                         v

                   CONTROLLERS

                         |

                         v

                     SERVICES

                         |

                         v

                   REPOSITORIES

                         |

                         v

                     DATABASE

```



### Layer Responsibilities

| Layer | Responsibility |

|---|---|

| **Controller** | Handles HTTP requests and responses |

| **DTO** | Defines request and response structures |

| **Service** | Contains application business logic |

| **Repository** | Handles persistence operations |

| **Model** | Defines application entities |

| **Security** | Handles authentication and authorization |

| **Config** | Configures application components |

| **Exception** | Handles application errors centrally |



### Request Processing Flow

1. The frontend sends an HTTP request.

2. Spring Security processes the incoming request.

3. The JWT filter validates authentication when required.

4. The controller receives the request.

5. Request data is mapped to the appropriate DTO.

6. The service performs business operations.

7. The repository accesses persistent data.

8. The service prepares the response.

9. The controller returns a JSON response.



---



## 🧰 Technology Stack



| Technology | Purpose |

|---|---|

| **Java 17+** | Backend programming language |

| **Spring Boot** | Backend application framework |

| **Spring Web** | REST API development |

| **Spring Security** | Authentication and authorization |

| **Spring Data JPA** | Persistence abstraction |

| **JWT** | Token-based authentication |

| **Maven / Gradle** | Build and dependency management |

| **JSON** | API data exchange |

| **WebSocket** | Bidirectional communication configuration |

| **JUnit** | Backend testing |



> **Database:** Configure the actual database according to the project's persistence dependency and `application.properties`. The supplied folder structure does not establish whether the application uses MySQL, PostgreSQL, or another database.



---



## 💻 Prerequisites



Before running the backend, install the following:



| Software | Recommended Version |

| **Java JDK** | 17 or newer |

| **Spring Boot** | Compatible with project configuration |

| **Maven** | 3.9+ if using Maven |

| **MySQL/PostgreSQL** | Only if configured by the project |

| **Git** | Latest stable |

| **IntelliJ IDEA / Eclipse**| Recommended IDE |

| **Postman** | API testing |

| **Docker** | Optional for containerization |



Verify Java:

```bash

java -version

```



Verify Maven:

```bash

mvn -version

```



---



## 🚀 Getting Started



### 1. Clone the Repository

```bash

git clone https://github.com/kushgope/cafe.git

cd cafe/backend

```



### 2. Configure Backend Environment

The backend currently includes:

```text

src/main/resources/application.properties

```

For local development, configure application properties or environment variables.



Recommended environment variables:

```env

SERVER_PORT=8080

JWT_SECRET=replace_with_a_long_random_secret

FRONTEND_URL=http://localhost:5173

DATABASE_URL=jdbc:mysql://localhost:3306/floracafe

DATABASE_USERNAME=root

DATABASE_PASSWORD=your_password

```

> **Important:** The database URL above is an example for MySQL, not a confirmation that MySQL is currently configured.



### 3. Configure `application.properties`

Example configuration for a Spring Boot application using environment-variable placeholders:

```properties

# Application Configuration

spring.application.name=floracafe



# Server Configuration

server.port=${SERVER_PORT:8080}



# JWT Configuration

jwt.secret=${JWT_SECRET}



# Frontend CORS Origin

app.frontend.url=${FRONTEND_URL:http://localhost:5173}



# Database Configuration

spring.datasource.url=${DATABASE_URL}

spring.datasource.username=${DATABASE_USERNAME}

spring.datasource.password=${DATABASE_PASSWORD}

```

*Note: The actual property names must match the configuration classes and services in your implementation. Add any JPA, Hibernate, and database-driver settings required by the actual project.*



### 4. Install Dependencies

If the project uses Maven:

```bash

mvn clean install

```

If the repository contains the Maven wrapper:

**Linux/macOS:**

```bash

./mvnw clean install

```

**Windows:**

```bash

mvnw.cmd clean install

```



---



## ⚙️ Environment Configuration



### Environment Variable Reference

| Variable | Description | Example |

| `SERVER_PORT` | Backend server port | `8080` |

| `JWT_SECRET` | Secret used for JWT signing | `Secure random secret` |

| `FRONTEND_URL` | Allowed frontend origin | `http://localhost:5173` |

| `DATABASE_URL` | Database connection URL | `JDBC URL` |

| `DATABASE_USERNAME` | Database username | `root` |

| `DATABASE_PASSWORD` | Database password | `Local database password` |



### Root-Level Environment Variables

If you use a root `.env` file for local development tooling, keep it separate from Spring Boot configuration unless a tool explicitly loads it.

```env

FRONTEND_URL=http://localhost:5173

BACKEND_URL=http://localhost:8080

```

Spring Boot does not automatically load an arbitrary root `.env` file. Supply environment variables through your shell, IDE, deployment platform, or an explicitly configured environment loader.



### Security Recommendations

* Never commit production credentials.

* Never expose JWT signing secrets.

* Use a sufficiently long, randomly generated JWT secret.

* Use separate development and production credentials.

* Restrict database access.

* Keep `.env` files in `.gitignore`.

* Use HTTPS in production.



---



## ▶️ Running the Application



### Option 1: Run Using Maven

Navigate to the backend directory:

```bash

cd backend

mvn spring-boot:run

```



### Option 2: Run Using Maven Wrapper

**Linux/macOS:**

```bash

./mvnw spring-boot:run

```

**Windows:**

```bash

mvnw.cmd spring-boot:run

```



### Option 3: Run the Built JAR

Build the application:

```bash

mvn clean package

```

Run the generated JAR:

```bash

java -jar target/*.jar

```



**Backend URL**

By default: `http://localhost:8080`



### Full-Stack Development

Run the frontend and backend in separate terminals.



**Terminal 1 – Backend**

```bash

cd backend

mvn spring-boot:run

```



**Terminal 2 – Frontend**

```bash

cd frontend

npm install

npm run dev

```

The React application communicates with the backend through the configured API base URL.



---



## 🔌 API Documentation



FloraCafe provides REST API controllers for its core business modules. The following table describes the controller responsibilities. Exact paths and HTTP methods should be verified against annotations in the source code.



### API Modules

| Controller | Responsibility |

|---|---|

| `AuthController` | User registration and login |

| `MenuController` | Menu item operations |

| `OrderController` | Customer order operations |

| `AdminController` | Administrative operations |

| `ShopController` | Shop configuration and availability |



### Expected API Operations

| Module | Operation | Description |

|---|---|---|

| **Auth** | Register | Create a customer account |

| **Auth** | Login | Authenticate a customer |

| **Menu** | Get menu | Retrieve available menu items |

| **Orders** | Create order | Submit a customer order |

| **Orders** | Get orders | Retrieve order information |

| **Orders** | Update order | Modify order information |

| **Admin** | Manage customers | Administrative customer operations |

| **Admin** | Manage orders | Administrative order operations |

| **Shop** | Get settings | Retrieve shop configuration |

| **Shop** | Update settings | Modify shop configuration |



### Example REST API Structure

| Method | Example Endpoint | Purpose |

|---|---|---|

| `POST` | `/api/auth/signup` | Register user |

| `POST` | `/api/auth/login` | Authenticate user |

| `GET` | `/api/menu` | Retrieve menu |

| `POST` | `/api/orders` | Create order |

| `GET` | `/api/orders` | Retrieve orders |

| `GET` | `/api/orders/{id}` | Retrieve order details |

| `PUT` | `/api/orders/{id}` | Update order |

| `GET` | `/api/shop` | Retrieve shop information |

| `GET` | `/api/admin/customers` | Retrieve customers |



### Example Login Request

```json

{

  "email": "customer@example.com",

  "password": "your_password"

}

```



### Example Order Request

```json

{

  "items": [

    {

      "menuItemId": 1,

      "quantity": 2

    }

  ]

}

```

*Note: The exact request fields must match `LoginRequestDto.java` and `OrderRequestDto.java`.*



### API Response Format

A successful authentication response may follow this general structure:

```json

{

  "token": "your-jwt-token",

  "message": "Login successful"

}

```



### API Documentation Tools

For improved API discoverability, consider integrating Swagger UI, OpenAPI 3, or Postman collections. If Springdoc OpenAPI is added and configured, Swagger UI is commonly available at:

```text

http://localhost:8080/swagger-ui/index.html

```



---



## 🔐 Authentication and Security



FloraCafe uses JWT-based authentication components.



### Security Components

| File | Responsibility |

|---|---|

| `SecurityConfig.java` | Spring Security configuration |

| `JwtAuthenticationFilter.java`| Processes incoming JWT authentication |

| `JwtService.java` | Generates and validates tokens |

| `CustomUserDetailsService.java`| Loads user details |

| `AuthService.java` | Handles authentication business logic |

| `HashGenerator.java` | Supports password hashing-related functionality |



### Authentication Workflow

```text

        USER LOGIN REQUEST

                |

                v

          AUTH CONTROLLER

                |

                v

           AUTH SERVICE

                |

                v

       USER DETAILS SERVICE

                |

                v

        PASSWORD VALIDATION

                |

                v

            JWT SERVICE

                |

                v

        JWT TOKEN RESPONSE

                |

                v

           FRONTEND APP

```



### Authorization

Protected requests should include a bearer token:

```http

Authorization: Bearer YOUR_JWT_TOKEN

```



### Security Best Practices

* Store passwords using a secure adaptive password-hashing algorithm such as BCrypt.

* Validate JWT signatures and expiration.

* Enforce role-based access at the API layer.

* Validate request DTOs using Bean Validation.

* Never trust client-supplied user IDs or roles for authorization.

* Restrict CORS to trusted origins.

* Avoid logging passwords, tokens, or sensitive personal data.

* Use secure secret management in production.

* Apply appropriate rate limits to authentication endpoints.



---



## 🗄️ Database and Data Initialization



The backend uses model and repository layers to represent and access application data.



### Domain Models

| Model | Description |

|---|---|

| `User.java` | Customer and administrative user information |

| `Role.java` | User role representation |

| `MenuItem.java` | Café menu item information |

| `Order.java` | Customer order information |

| `OrderItem.java` | Individual order line items |

| `OrderStatus.java` | Order status representation |

| `ShopSettings.java` | Shop configuration |



### Repository Layer

```text

repository/

├── UserRepository.java

├── MenuRepository.java

├── OrderRepository.java

└── ShopRepository.java

```



### Initial Menu Data

The project contains:

```text

src/main/resources/data/menu-items.json

```

It also includes:

```text

config/MenuDataInitializer.java

```

Ensure that initialization logic is idempotent so that application restarts do not unintentionally duplicate records.



### Database Recommendations

* Use a managed relational database for production.

* Configure database migrations with Flyway or Liquibase.

* Avoid destructive schema updates.

* Enable database backups.

* Use connection pooling.

* Keep database credentials outside source control.

* Configure indexes for frequently queried fields.



---



## ⚠️ Exception Handling



FloraCafe includes centralized exception handling through:

```text

exception/

└── GlobalExceptionHandler.java

```



**Recommended response structure:**

```json

{

  "timestamp": "2026-11-18T10:00:00",

  "status": 400,

  "error": "Bad Request",

  "message": "Invalid request data",

  "path": "/api/orders"

}

```



**Recommended exception handling practices:**

* Return appropriate HTTP status codes.

* Validate incoming requests.

* Avoid exposing stack traces.

* Log server-side errors securely.

* Return consistent error structures.



---



## 🧪 Testing



The backend includes a test source directory:

```text

src/test/java/com/floras/cafe/

└── CafeApplicationTests.java

```



**Run Tests**

```bash

mvn test

```



**Build and Test**

```bash

mvn clean verify

```



### Recommended Testing Coverage

| Test Type | Purpose |

|---|---|

| **Unit tests** | Validate service logic |

| **Controller tests** | Validate HTTP behavior |

| **Repository tests** | Validate persistence |

| **Security tests** | Validate authentication and authorization |

| **Integration tests** | Validate complete application workflows |



---



## 🌐 Deployment



### Option 1: Render

1. Push the backend source code to GitHub.

2. Create a new Web Service.

3. Select the backend repository or configure the backend root directory.

4. Configure the build command:

   ```bash

   mvn clean package -DskipTests

   ```

5. Configure the start command:

   ```bash

   java -jar target/*.jar

   ```

6. Add production environment variables and deploy.



### Option 2: AWS

The backend can be deployed using AWS Elastic Beanstalk, ECS, App Runner, or EC2.

| AWS Service | Purpose |

|---|---|

| **Elastic Beanstalk / ECS**| Application hosting |

| **RDS** | Managed relational database |

| **Secrets Manager** | Secure credentials |

| **CloudWatch** | Application monitoring |

| **ALB** | Traffic distribution |



### Option 3: Heroku

1. Connect the GitHub repository.

2. Configure the Java build environment.

3. Set environment variables.

4. Configure database connectivity and deploy.



### Production Environment Variables

```env

SERVER_PORT=8080

JWT_SECRET=your_production_secret

FRONTEND_URL=https://your-frontend-domain.com

DATABASE_URL=your_production_jdbc_url

DATABASE_USERNAME=your_database_username

DATABASE_PASSWORD=your_database_password

```



### Production Deployment Checklist

- [ ] Configure production database.

- [ ] Set secure JWT signing secret.

- [ ] Configure trusted CORS origins.

- [ ] Enable HTTPS.

- [ ] Disable unnecessary development settings.

- [ ] Configure production logging.

- [ ] Verify database migrations.

- [ ] Configure backups.

- [ ] Test authentication and authorization.



---



## 🧑‍💻 Best Practices



* Keep controllers lightweight.

* Place business logic inside service classes.

* Use DTOs instead of exposing persistence entities directly.

* Validate incoming request data.

* Keep repositories focused on data access.

* Use constructor-based dependency injection.

* Implement centralized exception handling.

* Enforce authorization at the backend.

* Use database transactions for critical operations.

* Maintain consistent naming conventions.



---



## 🐛 Troubleshooting



| Problem | Possible Solution |

|---|---|

| **Application fails to start** | Verify Java version and configuration |

| **Database connection error** | Check database URL and credentials |

| **JWT validation fails** | Verify signing secret and token expiration |

| **CORS error** | Configure frontend origin in SecurityConfig |

| **Port already in use** | Change SERVER_PORT |

| **Maven dependency error** | Run `mvn clean install` |

| **Menu data not loading** | Verify JSON resource and initializer |

| **Unauthorized response** | Check bearer token and security rules |

| **Order submission fails** | Verify request DTO and business validation |



---



## 🚀 Future Improvements



* Swagger/OpenAPI documentation.

* Database migration support.

* Docker containerization.

* Automated CI/CD pipeline.

* Refresh token implementation.

* Advanced role-based permissions.

* Online payment integration.

* Email and notification services.

* Improved order tracking.

* Audit logging.

* Automated integration testing.

* Production monitoring and observability.



---



## 👨‍💻 Author



**Kush Gope**  

Full-Stack Developer  

GitHub: [Kush Gope](https://github.com/KushGope/)

<p align="center">

Made with ❤️ by <strong>Kush Gope</strong>

</p>


 
