# Mini E-commerce Backend

A Java Spring Boot REST API for a simple e-commerce application with **products, cart, and authentication**.

##  Features

- **Products**
  - `GET /api/products` – list all products (pagination, sorting, filtering)
  - `GET /api/products/{id}` – get a single product by ID
  - `POST /api/products/add` – add a new product (**ADMIN only**)

- **Cart**
  - `POST /api/cart/add` – add product to cart
  - `GET /api/cart` – get current user’s cart
  - `PUT /api/cart/item/{id}` – update cart item quantity
  - `DELETE /api/cart/item/{id}` – remove item from cart

- **Authentication**
  - `POST /api/auth/register` – register a new user
  - `POST /api/auth/login` – login and receive a JWT token
  - JWT is used to protect all non-public routes

- **Security**
  - JWT authentication & authorization
  - Role-based access control (`USER`, `ADMIN`)
  - User-specific cart isolation

- **API Docs**
  - Swagger UI available at:
    ```
    http://localhost:8080/swagger-ui.html
    ```

---

## Tech Stack

- Java 21  
- Spring Boot 3.x  
- Spring Security + JWT  
- Spring Data JPA (PostgreSQL)  
- Lombok  
- Swagger / OpenAPI  
- Docker  

---

## Docker

- You have to install docker app
- To start everything you have to type `docker-compose up --build` in your temrminal
