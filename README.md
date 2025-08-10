```markdown
# Spring Security Application

This is a Spring Boot application that demonstrates user authentication and authorization using JWT (JSON Web Tokens). The application includes endpoints for user registration, login, and role-based access control.

## Features

- **User Registration**: Allows users to register with a username, password, and roles.
- **JWT Authentication**: Issues a JWT token upon successful login.
- **Role-Based Authorization**: Secures endpoints based on user roles (e.g., `ROLE_USER`, `ROLE_ADMIN`).
- **Unit Testing**: Includes tests for authentication and secured endpoints using `MockMvc`.

## Technologies Used

- **Java**: Programming language.
- **Spring Boot**: Framework for building the application.
- **Spring Security**: For authentication and authorization.
- **JWT**: For stateless authentication.
- **HikariCP**: Connection pooling for the database.
- **Maven**: Build tool.
- **JUnit 5**: For unit testing.

## Endpoints

### Authentication Endpoints
- `POST /auth/register`: Register a new user.
- `POST /auth/login`: Authenticate a user and receive a JWT token.

### Secured Endpoints
- `GET /api/user`: Accessible to authenticated users.
- `GET /api/admin`: Accessible only to users with the `ROLE_ADMIN`.

## Setup and Configuration

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```

2. **Configure the Database**:
   Update the database connection details in the `application.properties` file:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build the Project**:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**:
    - Register a user: `POST /auth/register`
    - Login to get a JWT token: `POST /auth/login`
    - Use the token to access secured endpoints.

## Testing

The project includes unit tests for authentication and secured endpoints. To run the tests:
```bash
mvn test
```

## Project Structure

- `src/main/java/com/saatvik/app`: Contains the main application code.
    - `jwt`: Handles JWT generation and validation.
    - `dto`: Data Transfer Objects for requests and responses.
    - `service`: Business logic for user management.
- `src/test/java/com/saatvik/app`: Contains unit tests for the application.

## Example Usage

### Register a User
```json
POST /auth/register
{
  "username": "admin",
  "password": "admin",
  "roles": ["ROLE_ADMIN"]
}
```

### Login
```json
POST /auth/login
{
  "username": "admin",
  "password": "admin"
}
```

### Access Secured Endpoint
```http
GET /api/user
Authorization: Bearer <JWT_TOKEN>
```

## License

This project is licensed under the MIT License.