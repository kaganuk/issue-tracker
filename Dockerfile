# Use a base image with Maven and JDK installed
FROM maven:3.8.3-openjdk-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the project's pom.xml and download dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy the entire project and build the JAR file
COPY . .
RUN mvn -B package

# Use a lightweight base image for the final image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file built in the previous stage
COPY --from=builder /app/target/issue-tracker-0.0.1-SNAPSHOT.jar .

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application when the container starts
CMD ["java", "-jar", "issue-tracker-0.0.1-SNAPSHOT.jar"]