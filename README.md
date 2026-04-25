# Blog Hub

Blog Hub is a Spring Boot REST API for a blogging platform. It supports user registration and login, JWT-based authentication, posts, categories, comments, likes, image uploads, password reset emails, and post search backed by Elasticsearch with a database fallback.

## Features

- User registration and login with JWT tokens
- User profile lookup, update, deletion, and profile image upload
- Category CRUD operations
- Blog post creation, listing, detail views, update, deletion, and image serving
- Pagination, sorting, and category filtering for posts
- Comments on posts
- Like and unlike posts for authenticated users
- Password reset flow with email tokens
- Elasticsearch indexing for posts after create, update, and delete events
- Search suggestions through Elasticsearch completion suggestions
- Swagger/OpenAPI UI via SpringDoc

## Tech Stack

- Java 17
- Spring Boot 3.5.10
- Spring Web
- Spring Data JPA
- Spring Security
- Spring Data Elasticsearch
- Spring Mail
- Spring Retry
- MySQL
- Maven
- Lombok
- ModelMapper
- JJWT
- SpringDoc OpenAPI

## Project Structure

```text
src/main/java/com/example/Blog/Hub
|-- config         # Security, CORS, mail sender, and application beans
|-- controller     # REST controllers
|-- dto            # Request/response DTOs
|-- entity         # JPA and Elasticsearch entities
|-- event          # Post indexing domain events
|-- exception      # Custom exceptions and global error handling
|-- filter         # JWT validation filter
|-- repository     # JPA and Elasticsearch repositories
|-- service        # Service interfaces
|-- service/impl   # Service implementations
|-- service/search # Search strategies and suggestions
`-- utills         # Constants and search query builder
```

## Prerequisites

- Java 17 or later
- Maven, or use the included Maven wrapper
- MySQL running locally or remotely
- Elasticsearch running on `http://localhost:9200`
- Gmail SMTP app password if you want the password reset email flow to work

## Configuration

The main configuration file is `src/main/resources/application.yml`.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog_hub
    username: springstudent
    password: springstudent
  elasticsearch:
    uris: http://localhost:9200
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

Before running the app:

1. Create a MySQL database named `blog_hub`.
2. Update the datasource username and password for your local database.
3. Start Elasticsearch on port `9200`.
4. Replace the mail username and password if password reset emails are needed.

## Run Locally

Clone the repository and start the application:

```bash
git clone <your-repository-url>
cd Blog-Hub
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The API runs on:

```text
http://localhost:8080
```

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

## Run Tests

```bash
./mvnw test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
```

## Authentication

Register or log in to receive a JWT token. Send the token in the `Authorization` header for endpoints that require authentication.

```http
Authorization: <jwt-token>
```

Current security configuration permits many read/write endpoints publicly, while `DELETE /posts/{postId}` is authenticated. Like operations also depend on the authenticated user in the service layer.

## API Overview

### Auth

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/register` | Register a new user and return a JWT |
| POST | `/login` | Log in and return a JWT |
| POST | `/forgetPassword` | Generate a password reset token and send email |
| GET | `/resetPassword?token={token}` | Validate a reset token |
| POST | `/resetPassword?token={token}` | Reset password using a token |

Example register request:

```json
{
  "name": "Nikhil Singh",
  "email": "nikhil@example.com",
  "password": "password123",
  "organisation": "Blog Hub",
  "about": "Spring Boot developer"
}
```

Example login request:

```json
{
  "email": "nikhil@example.com",
  "password": "password123"
}
```

### Users

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/user/{userId}` | Get user by ID |
| GET | `/user?username={email}` | Get user by email |
| PUT | `/user` | Update user profile with multipart form data |
| DELETE | `/user/{userId}` | Delete user |

`PUT /user` expects multipart form data with:

- `userDTO`: user JSON
- `image`: image file

### Categories

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/category` | Create category |
| GET | `/category` | List categories |
| GET | `/category/{id}` | Get category by ID |
| PUT | `/category` | Update category |
| DELETE | `/category/{id}` | Delete category |

Example category request:

```json
{
  "categoryTittle": "Spring Boot",
  "categoryDescription": "Articles about Spring Boot and Java backend development"
}
```

### Posts

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/posts/{categoryId}/{userId}` | Create a post for a user and category |
| GET | `/posts` | List posts with pagination, sorting, and optional category filter |
| GET | `/posts/{postId}` | Get post by ID and increment view count |
| GET | `/posts/user/{userId}` | List posts by user |
| PUT | `/posts` | Update post with multipart form data |
| DELETE | `/posts/{postId}` | Delete post |
| GET | `/posts/search/{query}` | Search posts by title using the database query |
| GET | `/posts/search` | Search posts using Elasticsearch with database fallback |
| GET | `/posts/suggest?suggest={prefix}` | Get post search suggestions |

Example create post request:

```json
{
  "tittle": "Getting Started with Spring Boot",
  "content": "Spring Boot makes it easy to create stand-alone, production-grade applications."
}
```

Example list posts request:

```text
GET /posts?pageNumber=0&pageSize=10&sort=date&category=-1
```

Example Elasticsearch search request:

```text
GET /posts/search?query=spring&sortBy=RECENT&sortDir=DESC&pageNumber=0&pageSize=10
```

Supported `sortBy` values:

- `RELEVANCE`
- `RECENT`
- `POPULAR`

Supported `sortDir` values:

- `ASC`
- `DESC`

### Comments

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/post/{postId}/comment` | Create comment on a post |
| GET | `/post/{postId}/comment/{commentId}` | Get comment by ID |
| GET | `/post/{postId}/comments` | List comments for a post |

Example comment request:

```json
{
  "comment": "This was helpful."
}
```

### Likes

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/posts/{postId}/like` | Toggle like/unlike for the current user |
| GET | `/posts/{postId}/like` | Check if the current user liked the post |

### Images

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/image/{imageName}` | Serve an uploaded image |

Uploaded images are stored in the root-level `image` directory. Image URLs are built with:

```text
http://localhost:8080/image/{imageName}
```

## Search Behavior

Post create, update, and delete operations publish events after the database transaction commits. Event listeners asynchronously update the `posts` Elasticsearch index and retry failed indexing work up to three times.

`GET /posts/search` first tries Elasticsearch. If Elasticsearch is unavailable or a data access error occurs, the service falls back to a database title search.