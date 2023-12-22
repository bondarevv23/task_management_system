package com.github.bondarevv23.task_management_system.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    @JsonProperty("access_token")
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiSm9obiBEb2UifQ.K1lVDxQYcBTPnWMTGeUa3gYAgdEhMFFv38VmOyl95bA")
    private String accessToken;

    @JsonProperty("expires_in")
    @Schema(example = "300")
    private Integer expiresIn;

    @JsonProperty("refresh_token")
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiSm9obiBEb2UifQ.K1lVDxQYcBTPnWMTGeUa3gYAgdEhMFFv38VmOyl95bA")
    private String refreshToken;

    @JsonProperty("refresh_expires_in")
    @Schema(example = "1800")
    private Integer refreshExpiresIn;
}
