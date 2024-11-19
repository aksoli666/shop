# Bookstore Application 📚🌟

Welcome to my exciting journey into the world of **Java** and **Spring Boot**! This project is a **modern, user-friendly online bookstore application** designed for both **book lovers** and **store managers**. It ensures a **seamless experience for buyers** and **efficient management for administrators**.

---

## About the Project 💻📚

The mission of this project is simple: make **book purchasing a delightful experience** while simplifying **store management**. Built using **robust technologies**, it features comprehensive functionalities for both **users** and **administrators**, making it both an enjoyable and practical tool.

---

## Domain Models 🏷️

The application is built around several key entities, which are the core building blocks of the system:

- **User ❤️**: Stores customer data and profiles, forming the heart of the system.
- **Role 🛠️**: Defines user roles, such as a **customer** or an **administrator**.
- **Book 📖**: The main product of the store, which can be browsed, searched, and purchased.
- **Category 📂**: Organizes books into thematic sections for easy navigation.
- **Shopping Cart 🛒**: Collects items a user plans to buy.
- **Shopping Cart Item 📦**: Represents a specific book in the cart.
- **Order 📦✨**: A completed purchase comprising one or more books.
- **Order Item 📝**: Represents a specific book in an order.

---

## UML ER Diagram for Bookstore Application 📚🌟

```plaintext
+---------------------+          +---------------------+         +---------------------+
|       User          |          |       Role          |         |       Book          |
+---------------------+          +---------------------+         +---------------------+
| - id: bigint        |          | - id: bigint        |         | - id: bigint        |
| - name: String      |          | - name: String      |         | - title: String     |
| - email: String     |          |                     |         | - author: String    |
| - password: String  |          |                     |         | - price: Decimal    |
+---------------------+          +---------------------+         | - category_id: UUID |
         |                                    |                  |                     |
         +----------------------+             +----------------> |                     |
                              (1:n) Role                         +---------------------+
                               Assignment
+----------------------+         
|    ShoppingCart      |
+----------------------+         
| - id: bigint         |          
| - user_id: bigint    |          
+----------------------+          
         |                                    
         +------------+ (1:n)
                      |
         +---------------------+           +---------------------+ 
         |  ShoppingCartItem   |           |      Category       |
         +---------------------+           +---------------------+ 
         | - id: bigint        |           | - id: bigint        |
         | - cart_id: bigint   |           | - name: String      |
         | - book_id: bigint   |           +---------------------+
         | - quantity: int     |                             
         +---------------------+         
                      |                             
                      +-----------------+ (1:n)      
                                       |
                         +---------------------+       
                         |      Order          |       
                         +---------------------+       
                         | - id: bigint        |       
                         | - user_id: bigint   |       
                         | - total_price: Dec. |       
                         | - created_at: Date  |       
                         +---------------------+       
                                       |                             
                                       +----------------+ (1:n)     
                                                    |
                                    +---------------------+       
                                    |     OrderItem       |       
                                    +---------------------+       
                                    | - id: bigint        |       
                                    | - order_id: bigint  |       
                                    | - book_id: bigint   |       
                                    | - quantity: int     |       
                                    +---------------------+
```   
---

## Roles 🧑‍💼

### **Buyer (User) 🛒📚✨**

- Explore, select, and purchase books with a **simple and intuitive interface**.
- Enjoy a **personalized shopping experience**.

### **Manager (Administrator) 💼📊🔧**

- Manage book inventory, create new categories, and process orders.
- Oversee the **overall functioning of the bookstore**, ensuring smooth operations.

---

## Core Functionalities 📝

### **For Buyers:**

- **Register & Login 🔐**: Simple account creation and authentication process.
- **Browse & Search 🔎**: Effortlessly find books using easy navigation or search by title.
- **Bookshelves 📚**: Access thematic sections with books organized by categories.
- **Shopping Cart 🛒**: Add, remove, and review items before making a purchase.
- **Checkout 💳**: Complete purchases with **order tracking** in your purchase history.

### **For Managers:**

- **Book Management 📚**: Add, update, and delete books from the store.
- **Category Management 📂**: Create, edit, and delete categories for easy organization.
- **Order Processing 📦**: Manage order statuses such as **shipped**, **delivered**, etc.

---

## Technology Stack 🚀

This project uses **cutting-edge technologies** to ensure **high performance** and a **smooth user experience**:

### **Core Application Framework**
- **Spring Boot 🌱**: To create a stable and scalable application.
- **Spring Security 🔒**: To provide robust **data and user protection**.
- **Spring Data JPA 🗂️**: To facilitate **seamless database interaction**.

### **Database Management**
- **SQL Database 🛢️:** Ensures reliable and efficient data storage and retrieval.
- **Liquibase 🔄: ** Manages database version control and streamlines migrations.

### **Data Transformation and Communication**
- **MapStruct 🚀:** Simplifies and accelerates object mapping tasks.
- **JWT (JSON Web Tokens) 🔐:** Implements secure authentication and session management.

### **Development and Documentation**
- **Swagger 📚**: For **comprehensive API documentation**.
- **GlobalExceptionHandler ⚠️:** Handles application-wide exceptions gracefully for robust error management.

### **Deployment and Scalability**
- **Docker 🐳:** Enables containerized deployment for consistency and portability.
- **AWS ☁️:** Offers cloud scalability, reliability, and high availability.

---

## Controllers and Their Responsibilities 🛠️

Each part of the system is controlled by its own dedicated controller, responsible for different functionalities:

- **User Controller 🧑‍💼**: Handles **user registration**, authentication, and profile management.
- **Book Controller 📚**: Manages adding, updating, and deleting books.
- **Category Controller 📖**: Controls bookshelf sections for easy categorization.
- **Shopping Cart Controller 🛒**: Manages all shopping cart operations.
- **Order Controller 📦**: Handles order creation, management, and updates.

---

# How to Run the Application Locally 🖥️🐳

Follow these steps to set up and run the application on your local machine. The application is containerized using **Docker**, making it easy to deploy and manage.

### Prerequisites 🛠️

Before you begin, ensure you have the following installed:

1. **Docker**: Install Docker Desktop from [Docker's official website](https://www.docker.com/products/docker-desktop).
2. **Java (JDK 17 or later)**: Download from [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html) or [OpenJDK](https://openjdk.org/).
3. **Git**: Install Git from [Git's official website](https://git-scm.com/).
4. **Postman (optional)**: For testing the API, download [Postman](https://www.postman.com/downloads/).

### Steps to Set Up and Run 🏃‍♂️

1. **Clone the Repository**:
   `git clone https://github.com/your-username/your-repo-name.git && cd your-repo-name`

3. **Build the Application**: Use Maven to build the application:
   `mvn clean package`

4. **Start Docker Containers**:
   Make sure Docker is running on your system, then execute the following command to start the application along with its dependencies (e.g., database):
   `docker-compose up --build`

5. **Stop the Application**: To stop the application and its containers, use:
   `docker-compose down`


### Additional Notes 📝
- **Database Configuration:** The application uses Liquibase for database version control. Ensure the database container is started properly as defined in the docker-compose.yml file.
- **Environment Variables:** Modify the .env file (if present) or application.yml to adjust environment-specific configurations (e.g., database credentials, port numbers).

### Testing the API with Postman 🔎
- Import the [Postman collection](https://www.postman.com/supply-astronomer-36769183/workspace/online-book-shop-my-first-time-with-postman) to test API endpoints.
- Set `localhost:8088` as the base URL for all requests.

### With these steps, your application should be up and running locally in no time!
---

---

# Demonstration 🎥


While most are familiar with the general idea of an online bookstore, this project showcases its functionality through **Postman**.


[**CLICK HERE**](https://olexsandradorofeieiva.wistia.com/medias/6fco5qik1i) to explore the world of possibilities where every book finds its reader! 📚✨

## Final Notes ✨

This project represents the culmination of my passion for **learning Java** and my desire to build something **impactful**. Along the way, mistakes and challenges only pushed me closer to **mastery**. 💡

Let’s **redefine** how people **discover** and **buy books** — one line of code at a time. 🚀

---

