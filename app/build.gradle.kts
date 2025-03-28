plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.asciidoctor.jvm.convert") version "3.3.0"
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

    // database
    runtimeOnly("com.mysql:mysql-connector-j")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    // api
    implementation("com.slack.api:slack-api-client:1.45.3")
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter:1.0.0-SNAPSHOT")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
}

// spring restdocs
apply(from = "restdocs.gradle")
