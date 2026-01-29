# Run Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the JAR built by GitHub Actions (CI)
COPY api-gateway/target/*.jar app.jar

# Expose port (default 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]