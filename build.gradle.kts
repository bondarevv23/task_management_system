plugins {
	java
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.github.bondarevv23"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

val mapstructVersion = "1.6.0.Beta1"
val lombokVersion = "1.18.30"
val lombokMapstructBindingVersion = "0.2.0"

dependencies {
	// spring
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-cache")

	// docs
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

	// databases
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.liquibase:liquibase-core")
	runtimeOnly("org.postgresql:postgresql")

	// tests
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.assertj:assertj-core:3.6.1")
	testImplementation("com.github.dasniko:testcontainers-keycloak:3.2.0")
	testImplementation("org.mockito:mockito-core:5.8.0")

	// filter
	implementation("com.turkraft.springfilter:jpa:3.1.5")

	// mapstruct and lombok
	implementation("org.mapstruct:mapstruct:${mapstructVersion}")
	implementation("org.projectlombok:lombok:${lombokVersion}")
	annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")
	annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${lombokMapstructBindingVersion}")
	testAnnotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")
	testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")

	// bucket4j
	implementation("com.giffing.bucket4j.spring.boot.starter:bucket4j-spring-boot-starter:0.10.1")

	// cache
	implementation("javax.cache:cache-api:1.1.1")
	implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
	implementation("com.github.ben-manes.caffeine:jcache:3.1.8")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
