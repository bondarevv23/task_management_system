package com.github.bondarevv23.task_management_system.model.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {
    @NotNull
    private Integer taskId;

    @NotBlank
    private String text;
}
