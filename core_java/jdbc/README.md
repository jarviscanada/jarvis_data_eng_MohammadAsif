# Introduction
(50-100 words)
What does this app do? What technoglies your have used? (JDBC, PSQL, MVN, etc..)

The Stock Quote Application is a Java-based program that allows users to simulate stock trading and build a portfolio using live stock data retrieved through a REST API (Alpha Vantage API). Users can view their portfolio, buy and sell stock options, and experience a realistic trading simulation without the risk of actual financial loss.

# Implementaiton
The application's development utilized Java, with lifecycle dependency management and build processes handled through Maven. User transactions and portfolio positions are securely stored in a PostgreSQL database, connected to the application via Java Database Connectivity (JDBC). The entire program has been containerized with Docker and is published on Docker Hub for seamless usability and portability.

To ensure reliability and robustness, extensive testing was performed using unit and integration tests, verifying the correctness of individual components and the overall system. Additionally, loggers (powered by SLF4J) were integrated to provide detailed runtime insights, allowing for efficient debugging and monitoring of user interactions and system operations.

## ER Diagram
ER diagram

## Design Patterns
Discuss DAO and Repository design patterns (150-200 words)

The program implements both the Data Access Object (DAO) and Repository design patterns to facilitate seamless data transfer between the application and the database. By using these specific patterns, the app achieves a clean separation of concerns, ensuring that data storage and retrieval logic are isolated from the core business logic. This approach enhances modularity as well as makes the 
program easier to maintain and extend over time.

The Quote and Position entities, which represent the user's stock-related data, are modeled as objects in the application. These entities have corresponding DAO classes that are specifically designed to handle Create, Read, Update, and Delete (CRUD) operations. Each DAO class is tailored to interact with a single database table, ensuring a focused and specialized approach to data manipulation. This separation simplifies debugging and enhances testability since each class has a well-defined responsibility.

By integrating the Repository pattern, the application gains an additional abstraction layer, allowing for flexible data handling that can adapt to changes in the underlying database or persistence mechanism. This design choice ensures that the application is both scalable and resilient to future enhancements. Overall, the combination of these design patterns leads to a robust and maintainable program.

# Test
I tested the application using a combination of unit and integration tests with JUnit and Mockito. For unit testing, I used Mockito to mock the database connection, allowing me to test classes in isolation without impacting a live database or encountering database-specific errors. Mocked ResultSets were created to simulate data from the database, providing the necessary Quote and Position details. For integration testing, I set up a PostgreSQL database in a Docker container, with tables for Quote and Position data sourced from user input or the Alpha Vantage API. These tests involved reading and writing sample data to the database to verify the correctness of DAO operations and the seamless interaction between the application and the database.
