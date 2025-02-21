plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.netflix.dgs.codegen") version "7.0.3"
    id("org.graalvm.buildtools.native") version "0.10.4"
}

group = "cloud.aquacloud"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-web-services")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("org.jcodec:jcodec:0.2.5")
    implementation("com.stripe:stripe-java:28.3.0")
    implementation("org.projectlombok:lombok:1.18.28")
    implementation("org.bytedeco:javacv-platform:1.5.6")
    implementation("com.stripe:stripe-java:28.3.0")
    implementation("ws.schild:jave-all-deps:3.3.1")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("com.aventrix.jnanoid:jnanoid:2.0.0")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.generateJava {
    schemaPaths.add("${projectDir}/src/main/resources/graphql-client")
    packageName = "cloud.aquacloud.aquacloudapi.codegen"
    generateClient = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}
