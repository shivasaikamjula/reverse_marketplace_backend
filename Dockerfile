# ==========================================
# Dockerfile for Spring Boot Java Backend
# Multi-stage build: Maven build -> JRE runtime
# ==========================================

# Stage 1: Build the application with Maven & JDK 17
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy application source code and compile package
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Lightweight production runtime environment
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create a non-root application user for container security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy the compiled JAR artifact from builder stage
COPY --from=builder /app/target/reverse-marketplace-backend-1.0.0.jar app.jar

# Configuration via environment variables (with fallback defaults)
ENV PORT=8080 \
    DB_URL=jdbc:mysql://mysql:3306/reverse_marketplace?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC \
    DB_USERNAME=root \
    DB_PASSWORD=root \
    JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970 \
    CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173

EXPOSE 8080

# Execute the Spring Boot JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
