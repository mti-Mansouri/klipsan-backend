# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Build the app using the Maven wrapper (skipping tests to speed up deployment)
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copy the jar file generated in Stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 (Render's default)
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]