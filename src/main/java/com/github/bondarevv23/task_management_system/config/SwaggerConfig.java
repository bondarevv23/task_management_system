package com.github.bondarevv23.task_management_system.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String JWT = "JWT";
    private static final String BEARER = "bearer";
    private static final String BEARER_AUTHENTICATION = "Bearer Authentication";
    private static final Info info;

    static {
        info = new Info()
                .title("TASK MANAGER API")
                .description("Secured api for creating, preforming and managing tasks")
                .version("v1")
                .contact(new Contact()
                        .name("Vladimir Bondarev")
                        .email("bondarevv9123@gmail.com")
                        .url("https://github.com/bondarevv23")
                );
    }

    @Bean
    public SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat(JWT)
                .scheme(BEARER);
    }

    @Bean
    public OpenAPI openAPI(SecurityScheme securityScheme) {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTHENTICATION))
                .components(new Components().addSecuritySchemes(BEARER_AUTHENTICATION, securityScheme))
                .info(info);
    }
}
