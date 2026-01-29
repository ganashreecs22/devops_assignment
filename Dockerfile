# syntax=docker/dockerfile:1

# Final runtime image (minimal)
FROM eclipse-temurin:17-jre-alpine AS base

# Security: create non-root user/group
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

WORKDIR /app

# Copy the fat jar from Maven build
COPY target/addressbook-*.jar app.jar

# Permissions & security
RUN chown -R appuser:appgroup /app
USER appuser:appgroup

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
