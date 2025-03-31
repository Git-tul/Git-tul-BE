FROM ubuntu:latest
LABEL authors="miensoap"

# Stage 1: Build the application
FROM gradle:7.5.1-jdk17 AS build
WORKDIR /src

# Copy Gradle configuration files
COPY build.gradle settings.gradle gradlew /src/
COPY gradle /src/gradle

# Download the dependencies
RUN ./gradlew dependencies

# Copy the module source code
COPY core /src/core
COPY infra /src/infra
COPY app /src/app

# Build the application
RUN ./gradlew :app:bootJar

# Stage 2: Extract layers
FROM openjdk:17-jdk-slim AS builder
WORKDIR /src
COPY --from=build /src/app/build/libs/app.jar /src/Git-tul-BE.jar

RUN java -Djarmode=layertools -jar Git-tul-BE.jar extract

# Stage 3: Create the runtime image
FROM openjdk:17-jdk-slim
WORKDIR /src

# Copy layers individually (캐싱 최적화)
COPY --from=builder /src/dependencies/ ./
COPY --from=builder /src/spring-boot-loader/ ./
COPY --from=builder /src/snapshot-dependencies/ ./
COPY --from=builder /src/application/ ./

# Expose the port the app runs on
EXPOSE 8080

# Set Timezone
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo "Asia/Seoul" > /etc/timezone

# Run the application
CMD ["java", "-Duser.timezone=Asia/Seoul", "org.springframework.boot.loader.launch.JarLauncher"]
