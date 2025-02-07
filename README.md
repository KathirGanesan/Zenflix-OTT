# ZenFlix

ZenFlix is an OTT (Over-the-Top) platform backend application inspired by Netflix, built using Java and Spring Boot. It supports video URL management, genre categorization, and subscription-based access. The project focuses on backend development and includes features like user management, subscriptions, and watchlists.

## Features

- **Video Management**: CRUD operations for videos, storing and managing video URLs, including genre-based categorization.
- **Genre Management**: Manage different genres for categorizing content.
- **User Management**: Registration, authentication (JWT-based), and profile management.
- **Subscription System**: Plans, activations, and renewals.
- **Watchlist**: Users can add or remove items from their watchlist.
- **Payment Integration**: Razorpay payment gateway for processing payments.
- **Payment Webhooks**: Razorpay webhooks handle post-payment updates, automatically updating transactions and subscriptions in the backend.
- **Database Management**: PostgreSQL as the database with Liquibase for schema migrations.
- **CI/CD & Deployment**: Dockerized application with AWS EC2 deployment.

## Tech Stack

- **Backend**: Java, Spring Boot, Spring Security (JWT), Hibernate
- **Database**: PostgreSQL, Liquibase
- **Containerization**: Docker
- **Deployment**: AWS EC2
- **Version Control**: Git
- **Payment Gateway**: Razorpay

## Deployment

ZenFlix is deployed on an AWS EC2 instance and accessible via the domain:
[https://www.zenflixapp.online/](https://www.zenflixapp.online/)

### API Access

- **Production API Base URL**: `https://www.zenflixapp.online/api`
- **Local API Base URL**: `http://localhost:8080/api`

### Postman Collection

You can use the Postman collection for both AWS and local environments. Import the collection from the provided link or JSON file.

## User Access Control

To use ZenFlix, follow these steps:

1. **Register** using `/auth/register` API.
2. **Login** using `/auth/login` API to get a JWT token.
3. Use the JWT token in the request headers to access protected endpoints.

### Access Levels

- **Unsubscribed Users**:
  - Can access only public trailers API.
  - Can subscribe using the subscription API.
- **Subscribed Users**:
  - Can access all content-related APIs, including full video URLs and watchlist management.

## Installation & Setup

### Prerequisites

- Install Java 17+
- Install Maven
- Install PostgreSQL
- Install Docker (optional for containerized setup)

### Steps

1. **Clone the repository**:

   ```sh
   git clone <repository-url>
   cd zenflix
   ```

2. **Configure database**:

   - Ensure PostgreSQL is installed and running.
   - Create a database for ZenFlix.
   - Ensure environment variables are set up properly in a `.env` file and update `application.properties` accordingly.

3. **Run database migrations**:

   ```sh
   ./mvnw liquibase:update
   ```

4. **Build and run the application**:

   - Load environment variables:
     ```sh
     set -a && source .env && set +a
     ```
   - Build and run the application:
     ```sh
     ./mvnw clean install package
     ./mvnw spring-boot:run
     ```

   5. **Run using Docker Compose**:

   - Verify that Docker is installed and running:
     ```sh
     docker --version && docker info
     ```
   - Start all containers using Docker Compose:
     ```sh
     docker-compose up -d
     ```
   - Stop and remove containers when needed:
     ```sh
     docker-compose down
     ```

   - Verify that Docker is installed and running:
     ```sh
     docker --version && docker info
     ```
   - Start all containers using Docker Compose:
     ```sh
     docker-compose up -d
     ```
   - Stop and remove containers when needed:
     ```sh
     docker-compose down
     ```

## API Endpoints

- **User Authentication**: `/auth/register`, `/auth/login`
- **Genre**: `/genres` (CRUD)
- **Role**: `/roles` (CRUD)
- **User**: `/users` (CRUD)
- **User Subscription**: `/user-subscriptions` (Subscribe, Unsubscribe, Get subscription by ID, Get all)
- **Video**: `/videos` (CRUD, Store and Retrieve Video URLs, Genre-wise videos)
- **Watchlist**: `/watchlist` (CRD)
- **Subscription Plan**: `/subscription-plans` (CRUD)
- **Transaction**: `/transactions` (Get all, Get by ID)

## Future Enhancements

- **Full UI development using ReactJS**
- **Advanced analytics and reporting**
- **More third-party payment integrations**
- **Auto-scaling deployment on AWS**

## Contributing

Contributions are welcome! Fork the repository, create a branch, and submit a PR.

## License

This project is licensed under the MIT License.

