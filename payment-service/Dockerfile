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
ENV PORT=8080
EXPOSE $PORT
COPY --from=test-stage /app/build/libs/*.jar app.jar
COPY /src/main/resources/application-mysql.properties /app/config/application-mysql.properties
ENV SPRING_PROFILES_ACTIVE=mysql
ENV SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL
ENV SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME
ENV SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.additional-location=file:/app/config/application-mysql.properties"]