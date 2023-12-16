package com.github.bondarevv23.task_management_system.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotNull
    @JsonProperty("refresh_token")
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiSm9obiBEb2UifQ.K1lVDxQYcBTPnWMTGeUa3gYAgdEhMFFv38VmOyl95bA")
    private String refreshToken;
}
