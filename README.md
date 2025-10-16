# Demo project

This is a demo project to showcase the structure and setup of a typical software project. It includes various components such as documentation, source code, tests, and configuration files.

## Task
The task is attached as a pdf named `task.pdf`.

## Project Structure
1. `controllers`: Package that contains all the controllers with defined endpoints that are available.
2. `model`: package that contains all the entities, dtos and enums used in the project.
3. `repository`: package that contains all the repositories used to interact with the database.
4. `service`: package that contains all the services used to implement the business logic.

## Getting Started
To get started with this project, follow these steps:
1. Clone the repository to your local machine.
2. Check if you have Java 21 and Maven installed.
3. Start docker (for non-linux users).
4. Run `docker-compose up` to start the database.
5. Run `mvn clean install` to build the project.
6. Run `mvn spring-boot:run` to start the application.
7. `resources` folder contains postman collection to test the endpoints.