# Build Stage
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /app
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src
RUN ./gradlew build --no-daemon --exclude-task test