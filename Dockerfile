# Base image with JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Create a directory for the app
VOLUME /tmp

# Copy the jar file from target folder (after Maven build)
COPY target/*.jar app.jar

# Command to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app.jar"]
