package com.github.bondarevv23.task_management_system.model.api;

import com.github.bondarevv23.task_management_system.model.Priority;
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
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private UserDTO performer;

    @NotNull
    private Priority priority;
}
