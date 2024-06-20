# Use the official OpenJDK 17 image from the Docker Hub
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build output (JAR file) into the container
COPY target/university-0.0.1-SNAPSHOT.jar app.jar

# Specify the command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
