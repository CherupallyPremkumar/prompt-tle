# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy parent pom and flattened poms if they exist (handling the complex structure)
COPY pom.xml .
COPY .flattened-pom.xml .

# Copy all modules
COPY shared-library shared-library
COPY storage-service-api storage-service-api
COPY storage-service-infra storage-service-infra
COPY quota-service-api quota-service-api
COPY quota-service-impl quota-service-impl
COPY upload-service upload-service
COPY query-service query-service
COPY notification-service notification-service
COPY analytics-service analytics-service
COPY auth-service auth-service
COPY prompt-service prompt-service
COPY api-gateway api-gateway

# Build the project (skip tests to speed up, assuming tests ran in CI)
RUN mvn clean package -DskipTests -pl api-gateway -am

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/api-gateway/target/*.jar app.jar

# Expose port (default 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
