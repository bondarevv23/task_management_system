package com.github.bondarevv23.task_management_system.model.api;

import com.github.bondarevv23.task_management_system.model.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRequest {
    @NotBlank
    @Size(max = 255)
    @Schema(example = "task title")
    private String title;

    @NotBlank
    @Schema(example = "task description")
    private String description;

    @NotNull
    private UserDTO performer;

    @NotNull
    private Priority priority;
}
