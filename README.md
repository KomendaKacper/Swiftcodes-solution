# Swiftcodes Solution

A Spring Boot-based application designed to manage and validate SWIFT codes efficiently. The project uses Docker to containerize the application and the database for easy setup and deployment. Swagger is integrated for detailed API documentation.

---

## Features
- **SWIFT Code Validation**: Validate and manage SWIFT codes for financial institutions.
- **RESTful APIs**: Provides endpoints for CRUD operations on SWIFT codes.
- **Containerized Deployment**: The application and MySQL database are fully containerized using Docker.
- **Swagger Integration**: Interactive API documentation via Swagger UI.
- **Automated Tests**: Includes unit tests to ensure functionality and reliability.
![image](https://github.com/user-attachments/assets/5ab3abbf-cf23-40a4-aafb-89a36b700be1)
---

## Prerequisites

Before running the project, ensure you have the following:

- **Docker** (version 20.x or higher)  
  Required for running the application and database in containers.  
  [Install Docker](https://docs.docker.com/get-docker/)
  
- **Docker Compose** (version 2.x or higher)  
  Used to orchestrate multiple containers.  
  [Install Docker Compose](https://docs.docker.com/compose/install/)

- **Java 21**  
  Required to run the application locally.  
  [Install Java](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)

- **Maven** (version 3.8 or higher)  
  Needed to build and manage dependencies.  
  [Install Maven](https://maven.apache.org/install.html)

---

## Getting Started

### Option 1: Running Locally
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/KomendaKacper/Swiftcodes-solution.git
   cd Swiftcodes-solution
   ```

2. **Set Up the Database**:
   - Ensure MySQL is installed and running locally.
   - Create a database named `swiftcodes`:
     ```sql
     CREATE DATABASE swiftcodes;
     ```
   - Configure database credentials in `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/swiftcodes
     spring.datasource.username=yourUsername
     spring.datasource.password=yourPassword
     ```

3. **Run the Application**:
   - Build the project:
     ```bash
     mvn clean install
     ```
   - Start the application:
     ```bash
     mvn spring-boot:run
     ```
   - The application will be available at [http://localhost:8080](http://localhost:8080).

---

### Option 2: Running with Docker
1. **Start the Application**:
   ```bash
   docker-compose up --build
   ```
2. The application will be accessible at [http://localhost:8080](http://localhost:8080).
3. The database is automatically started as a separate container and preconfigured to work with the application.

---

## Running Tests

Unit tests are located in the `src/test` directory. To run the tests:

   ```bash
   mvn test
   ```
![image](https://github.com/user-attachments/assets/1b59dcf3-b985-4fde-b5ac-c70707a84e32)

Tests validate the application's core functionalities to ensure proper operation.

---

## API Documentation

The project includes **Swagger UI** for API documentation. Once the application is running, you can access the documentation at:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

This documentation includes all endpoints, their parameters, and example responses.

---

## Docker Setup Details

The application uses Docker Compose to orchestrate the following containers:
1. **Application**: A Spring Boot application container.
2. **Database**: A MySQL database container preconfigured to connect with the application.

### Docker Compose Commands:
- **Start the containers**:
  ```bash
  docker-compose up --build
  ```
- **Stop the containers**:
  ```bash
  docker-compose down
  ```

### Default Configuration:
- **MySQL Database**:
  - Host: `mysql`
  - Port: `3306`
  - Database: `swiftcodes`
  - Username: `springstudent`
  - Password: `springstudent`

These settings are preconfigured in `docker-compose.yml` and `application.properties`.

---

## Project Structure

```plaintext
Swiftcodes-solution/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.example.swiftcodes/  # Application source code
│   │   ├── resources/
│   │       └── application.properties  # Application configuration
│   └── test/
│       └── java/
│           └── com.example.swiftcodes/  # Unit tests
├── docker-compose.yml                   # Docker Compose configuration
├── Dockerfile                           # Dockerfile for the application
├── pom.xml                              # Maven configuration
└── README.md                            # Project documentation
```

---


## License
This project is open source and available under the [MIT License](LICENSE).

---
