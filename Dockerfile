FROM ubuntu:latest
LABEL authors="miensoap"

# Stage 1: Build the application
FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app

# Copy the build.gradle and settings.gradle files
COPY build.gradle settings.gradle /app/

# Copy the gradle wrapper
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Download the dependencies
RUN ./gradlew dependencies

# Copy the rest of the application code
COPY core /app/core
COPY app /app/app


# Build the application
RUN ./gradlew :app:build

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/app/build/libs/app-0.0.1-SNAPSHOT.jar /app/Git-tul-BE.jar

# Expose the port the app runs on
EXPOSE 8080

# Set Tiemzone
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo "Asia/Seoul" > /etc/timezone

# Run the application
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "Git-tul-BE.jar"]
