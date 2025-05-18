# Công Nghệ Sử Dụng Trong Dự Án Bookstore

## 1. Frontend

### 1.1. Ngôn Ngữ và Framework

- **JavaScript/JSX**

  - ES6+ features
  - Arrow functions
  - Destructuring
  - Async/await
  - Template literals
  - Modules

- **ReactJS**
  - Functional Components
  - Hooks (useState, useEffect, useContext)
  - Custom Hooks
  - Context API
  - React.memo và useMemo
  - Error Boundaries

### 1.2. Quản Lý Gói

- **npm/yarn**
  - Package.json
  - Dependencies
  - DevDependencies
  - Scripts
  - Lock files

### 1.3. Công Cụ Hỗ Trợ

#### React Router

- Route Configuration
  ```jsx
  <Routes>
    <Route path="/" element={<Home />} />
    <Route path="/books" element={<BookList />} />
    <Route path="/books/:id" element={<BookDetail />} />
    <Route path="/cart" element={<Cart />} />
    <Route path="/checkout" element={<Checkout />} />
  </Routes>
  ```
- Navigation
  - useNavigate hook
  - Link component
  - Programmatic navigation
- Route Guards
  - Protected routes
  - Authentication checks
- Nested Routes
  - Layout components
  - Outlet component

#### Axios

- API Configuration
  ```javascript
  const api = axios.create({
    baseURL: "http://localhost:8080/api",
    timeout: 5000,
    headers: {
      "Content-Type": "application/json",
    },
  });
  ```
- Interceptors
  - Request interceptor
  - Response interceptor
  - Error handling
- API Calls
  - GET requests
  - POST requests
  - PUT requests
  - DELETE requests
- Error Handling
  - Global error handling
  - Custom error messages
  - Retry mechanism

#### TailwindCSS

- Configuration
  ```javascript
  module.exports = {
    content: ["./src/**/*.{js,jsx,ts,tsx}"],
    theme: {
      extend: {
        colors: {
          primary: "#1a365d",
          secondary: "#2d3748",
        },
      },
    },
    plugins: [],
  };
  ```
- Utility Classes
  - Flexbox
  - Grid
  - Spacing
  - Typography
  - Colors
- Custom Components
  - Buttons
  - Cards
  - Forms
  - Navigation
- Responsive Design
  - Breakpoints
  - Mobile-first approach
  - Custom media queries

## 2. Backend

### 2.1. Ngôn Ngữ và Framework

- **Java**

  - Java 17
  - Lambda expressions
  - Stream API
  - Optional class
  - Records
  - Pattern matching

- **Spring Boot**
  - Auto-configuration
  - Starter dependencies
  - Embedded servers
  - Actuator
  - DevTools

### 2.2. Các Thành Phần Tích Hợp

#### Spring Web

- REST Controllers
  ```java
  @RestController
  @RequestMapping("/api/books")
  public class BookController {
    @GetMapping
    public List<Book> getAllBooks() { ... }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) { ... }

    @PostMapping
    public Book createBook(@RequestBody Book book) { ... }
  }
  ```
- Request Mapping
  - Path variables
  - Request parameters
  - Request body
  - Response entities
- Exception Handling
  - Global exception handler
  - Custom exceptions
  - Error responses

#### Spring Data JPA

- Entity Classes
  ```java
  @Entity
  @Table(name = "books")
  public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
  }
  ```
- Repositories
  - CRUD operations
  - Custom queries
  - Query methods
  - Pagination
- Relationships
  - One-to-One
  - One-to-Many
  - Many-to-Many
- Transactions
  - Transaction management
  - Isolation levels
  - Propagation behaviors

#### Spring Security

- Security Configuration
  ```java
  @Configuration
  @EnableWebSecurity
  public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
      http
        .authorizeRequests()
        .antMatchers("/api/public/**").permitAll()
        .antMatchers("/api/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
        .and()
        .csrf().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
      return http.build();
    }
  }
  ```
- Authentication
  - JWT authentication
  - OAuth2
  - Basic authentication
- Authorization
  - Role-based access
  - Method security
  - Custom security rules
- Password Encryption
  - BCrypt
  - Password encoding
  - Salt generation

### 2.3. Build Tool

- **Maven/Gradle**
  - Dependencies
  - Plugins
  - Build lifecycle
  - Profiles
  - Properties

## 3. Quản Lý Mã Nguồn

### 3.1. Git

- **Công Cụ**

  - Git CLI
  - GitHub Desktop
  - VS Code Integration

- **Nền Tảng**
  - GitHub
  - GitLab
  - Bitbucket

### 3.2. Mô Hình Nhánh

- **Git Flow**
  ```
  main
    └── develop
        ├── feature/user-authentication
        ├── feature/book-management
        └── feature/order-processing
  ```
- Branch Types
  - main: Production code
  - develop: Development code
  - feature: New features
  - release: Release preparation
  - hotfix: Bug fixes

### 3.3. Quy Trình Làm Việc

1. Tạo nhánh feature từ develop
2. Phát triển tính năng
3. Tạo pull request
4. Code review
5. Merge vào develop
6. Tạo release từ develop
7. Merge vào main

## 4. Docker

### 4.1. Containerization

- **Dockerfile**

  ```dockerfile
  # Backend
  FROM openjdk:17-jdk-slim
  WORKDIR /app
  COPY target/*.jar app.jar
  EXPOSE 8080
  ENTRYPOINT ["java", "-jar", "app.jar"]

  # Frontend
  FROM node:16-alpine
  WORKDIR /app
  COPY package*.json ./
  RUN npm install
  COPY . .
  RUN npm run build
  EXPOSE 3000
  CMD ["npm", "start"]
  ```

### 4.2. Docker Compose

- **docker-compose.yml**

  ```yaml
  version: "3.8"
  services:
    backend:
      build: ./backend
      ports:
        - "8080:8080"
      depends_on:
        - db
        - redis

    frontend:
      build: ./frontend
      ports:
        - "3000:3000"
      depends_on:
        - backend

    db:
      image: postgres:13
      environment:
        POSTGRES_DB: bookstore
        POSTGRES_USER: admin
        POSTGRES_PASSWORD: secret

    redis:
      image: redis:6
      ports:
        - "6379:6379"
  ```

### 4.3. Triển Khai

- Build images
- Push to registry
- Pull on server
- Run containers
- Health checks
- Logging
- Monitoring
