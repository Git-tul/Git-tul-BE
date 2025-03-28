FROM ubuntu:latest
LABEL authors="miensoap"

# Stage 1: Build the application
FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app

# Copy Gradle configuration files
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Download the dependencies
RUN ./gradlew dependencies

# Copy the rest of the application code
COPY core /app/core
COPY app /app/app

# Build the application
RUN ./gradlew :app:bootJar

# Stage 2: Extract layers
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app
COPY --from=build /app/app/build/libs/app.jar /app/Git-tul-BE.jar

RUN java -Djarmode=layertools -jar Git-tul-BE.jar extract

# Stage 3: Create the runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy layers individually (캐싱 최적화)
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

# Expose the port the app runs on
EXPOSE 8080

# Set Timezone
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo "Asia/Seoul" > /etc/timezone

# Run the application
CMD ["java", "-Duser.timezone=Asia/Seoul", "org.springframework.boot.loader.launch.JarLauncher"]
