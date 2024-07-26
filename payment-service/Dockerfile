# Base Stage
FROM gradle:7.6.1-jdk17 AS base
WORKDIR /app
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

# Build Stage
FROM base AS build-stage
RUN ./gradlew build --no-daemon --exclude-task test

# Test Stage
FROM build-stage AS test-stage
RUN ./gradlew test --no-daemon

# Runtime Stage
FROM openjdk:17-jdk-slim AS runtime-stage
WORKDIR /app
EXPOSE 8080
COPY --from=test-stage /app/build/libs/*.jar app.jar
COPY /src/main/resources/application-mysql.properties /app/config/application-mysql.properties
ENV SPRING_PROFILES_ACTIVE=mysql
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.additional-location=file:/app/config/application-mysql.properties"]