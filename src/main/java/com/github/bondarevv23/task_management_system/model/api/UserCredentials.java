package com.github.bondarevv23.task_management_system.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCredentials {
    @NotNull
    @Email
    @Schema(example = "mail@example.com")
    private String email;

    @NotNull
    @Schema(example = "secret-password")
    private String password;
}
