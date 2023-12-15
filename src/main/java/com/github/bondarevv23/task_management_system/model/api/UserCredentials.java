package com.github.bondarevv23.task_management_system.model.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCredentials {
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}
