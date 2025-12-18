# syntax=docker/dockerfile:1.7

# Multi-stage build for Spring Boot application
FROM eclipse-temurin:21-jdk-alpine AS builder

# Install Maven
RUN apk add --no-cache maven

# Set working directory
WORKDIR /app

# Copy Maven configuration files
COPY pom.xml .

# Download dependencies (cache layer - reuses ~/.m2 between builds)
RUN --mount=type=cache,id=bc-finances-backend-m2,target=/root/.m2 mvn -B dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN --mount=type=cache,id=bc-finances-backend-m2,target=/root/.m2 mvn -B clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine AS runtime

# Install curl for health check
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -g 1001 -S bcfinances && \
    adduser -S bcfinances -u 1001 -G bcfinances

# Set working directory
WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R bcfinances:bcfinances /app

# Switch to non-root user
USER bcfinances

# Expose port
EXPOSE 8080

# Health check using Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM tuning for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
