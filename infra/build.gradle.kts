import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

dependencies {
    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    // api
    implementation("com.slack.api:slack-api-client:1.45.3")

    implementation("org.springframework.ai:spring-ai-core:1.0.0-M6")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter:1.0.0-M6")
}

tasks.withType<BootJar> {
    enabled = false
}
