package com.github.bondarevv23.task_management_system.util;

import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationEnvironment {
    public static JdbcDatabaseContainer<?> DB_CONTAINER;
    public static KeycloakContainer KEYCLOAK;

    @LocalServerPort
    private int port;

    protected WebClient client;

    @BeforeAll
    void initClient() {
        client = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    static {
        KEYCLOAK = new KeycloakContainer()
                .withRealmImportFile("keycloak-config/import/tasks-manager-realm.json");
        KEYCLOAK.start();
    }

    static {
        DB_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("postgres-test")
                .withUsername("username")
                .withPassword("password");
        DB_CONTAINER.start();
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DB_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", DB_CONTAINER::getUsername);
        registry.add("spring.datasource.password", DB_CONTAINER::getPassword);
    }

    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> KEYCLOAK.getAuthServerUrl() + "/realms/tasks-manager");
        registry.add("app-config.keycloak-token-url",
                () -> KEYCLOAK.getAuthServerUrl() + "/realms/tasks-manager/protocol/openid-connect/token");
    }

    protected String getAccessToken(UserCredentials credentials) {
        return client.post()
                .uri("api/v1/users/get-token")
                .bodyValue(credentials)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block().getAccessToken();
    }
}
