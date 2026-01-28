# Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
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
RUN mvn clean package -DskipTests -pl api-gateway -am

# Run Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/api-gateway/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xms512m", "-Xmx1024m", "-jar", "app.jar"]