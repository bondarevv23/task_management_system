package com.github.bondarevv23.task_management_system.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app-config")
public class ApplicationConfig {

    @NotBlank
    String keycloakTokenUrl;

    @Bean
    public WebClient keycloakWebClient() {
        return WebClient.builder()
                .baseUrl(keycloakTokenUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }
}
