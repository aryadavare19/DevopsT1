# ─────────────────────────────────────────────────────────────
# Stage 1: Build the JAR using Maven
# ─────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom.xml first so Maven dependencies are cached in Docker layer
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Build the JAR, skip tests (tests already run in CI)
RUN mvn package -DskipTests -B

# ─────────────────────────────────────────────────────────────
# Stage 2: Run the JAR in a slim JRE image
# ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /build/target/*.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
