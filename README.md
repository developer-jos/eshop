
# Eshop API

This is a Spring Boot application that provides APIs for handling location-based suggestions and product-related requests. The application uses Spring Boot 3, JPA for database interactions, and Springdoc OpenAPI for API documentation.

## Table of Contents

- [Project Structure](#project-structure)
- [Dependencies](#dependencies)
- [How to Run](#how-to-run)
- [API Endpoints](#api-endpoints)
- [Swagger Documentation](#swagger-documentation)
- [Testing](#testing)

## Project Structure

The project is structured as follows:

```
src/
│
├── main/
│ ├── java/com/eshop/
│ │ ├── controller/
│ │ │ ├── LocationController.java
│ │ │ ├── ProductController.java
│ │ ├── dto/
│ │ │ ├── PaginatedResponseDTO.java
│ │ │ ├── ProductResponseDTO.java
│ │ ├── entity/
│ │ │ ├── Location.java
│ │ │ ├── Product.java
│ │ ├── repository/
│ │ │ ├── LocationRepository.java
│ │ │ ├── ProductRepository.java
│ │ ├── service/
│ │ │ ├── LocationService.java
│ │ │ ├── ProductService.java
│ └── resources/
│ └── application.properties
│
└── test/
├── java/com/eshop/
│ ├── controller/
│ │ ├── LocationControllerTest.java
│ │ ├── ProductControllerTest.java
│ ├── service/
│ │ ├── LocationServiceTest.java
│ │ ├── ProductServiceTest.java
```

## Dependencies

- Spring Boot 3: The core framework used to build the application.
- Spring Data JPA: Used for database interactions.
- Springdoc OpenAPI: For generating Swagger documentation.
- JUnit 5: For testing.
- Mockito: For mocking in unit tests.

## How to Run

1. Clone the project.
2. Build the application using the `mvn clean install` command.
3. Start the PostgreSQL database on your local machine.
4. Execute the 4 scripts one by one located under `resources/static/eshop.sql`.
5. Provide the database URL, username, and password in the `application.properties` file.
6. Start the application using the following command:

   ```bash
   mvn spring-boot:run
   ```

7. Now you can access the Swagger UI for API documentation at:

   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## API Endpoints

### Location API

#### 1. Get Place Suggestions

- Endpoint: /api/v1/places/suggestions
- Method: GET
- Parameters:
  - input (String): The search input (required).
  - latitude (double): The latitude of the location (required).
  - longitude (double): The longitude of the location (required).
  - radius (int): The radius to search within (optional, default: 500000).
- Response: A list of place suggestions based on the input and location.

Example Request:

```bash
GET /api/v1/places/suggestions?input=park&latitude=12.9716&longitude=77.5946&radius=500000
```

### Product API

#### 2. Get Products by Location Name

- Endpoint: /api/v1/products/{location-name}
- Method: GET
- Path Parameter:
  - location-name (String): The name of the location (required).
- Query Parameter:
  - pageable: Pagination information (optional).
- Response: A paginated list of products for the specified location.

Example Request:

```bash
GET /api/v1/products/Bangalore?page=0&size=10
```

## Swagger Documentation

To access the Swagger UI for API documentation, navigate to:

```
http://localhost:8080/swagger-ui/index.html
```

You can also access the OpenAPI specification in YAML format:

```
http://localhost:8080/v3/api-docs.yaml
```

## Testing

Unit tests are written for both controllers and services using JUnit 5 and Mockito. To run the tests:

```bash
mvn test
```

### Running Specific Tests

To run tests for a specific class, use:

```bash
mvn -Dtest=LocationServiceTest test
```

## Additional Notes

- Ensure you have a valid Google API key for location-based suggestions and add it to your `application.properties` file:

```properties
google.api.key=YOUR_GOOGLE_API_KEY
```

- The project uses pagination for the product API, so make sure to provide appropriate pagination parameters.

- Exception handling is implemented to ensure meaningful error messages are returned in case of invalid inputs or other errors.
