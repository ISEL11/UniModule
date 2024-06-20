# University Management Application

## Overview

This application is a Spring Boot project with a MySQL database.
It includes functionalities to manage universities and their associated course modules.
This README provides instructions on how to set up, run, and test the application.

## Prerequisites

Ensure you have the following installed:

* Docker
* Docker Compose
* Java JDK 17
* Maven 

## Setup

### Building the Docker Image

First, build the application JAR file using Maven:

#### **`mvn clean install`**

Then, build the Docker image:
To start the application along with the MySQL database, run:

#### **`docker-compose up --build`**

## This will start two services:

* mysql: The MySQL database.
* university-management: The Spring Boot application.

The application will be accessible at http://localhost:8080.



## Application Configuration

The application uses the following environment variables for configuration:

* SPRING_DATASOURCE_URL: jdbc:mysql://localhost:3306/mydatabase
* SPRING_DATASOURCE_USERNAME: root
* SPRING_DATASOURCE_PASSWORD: new_password
* SPRING_JPA_HIBERNATE_DDL_AUTO: update
* SPRING_JPA_SHOW_SQL: true
* SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT: org.hibernate.dialect.MySQL8Dialect
* SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL: true

These variables are defined in the **docker-compose.yml** file.

## Running Tests

Integration tests are located in the `src/test/java/com/example/demo` directory.

You can run them using Maven:
**`mvn verify`**

## API Endpoints

### University Endpoints

* GET /api/universities/all: Get all universities.
* GET /api/universities/{id}: Get a university by ID.
* POST /api/universities: Create a new university.
* PUT /api/universities/{id}: Update an existing university.
* DELETE /api/universities/{id}: Delete a university by ID.
* GET /api/universities/search: Search for universities by name.

### Course Module Endpoints

* GET /api/universities/{universityId}/modules: Get all course modules for a university.
* GET /api/universities/{universityId}/modules/{id}: Get a course module by ID.
* POST /api/universities/{universityId}/modules: Create a new course module.
* PUT /api/universities/{universityId}/modules/{id}: Update an existing course module.
* DELETE /api/universities/{universityId}/modules/{id}: Delete a course module by ID.
