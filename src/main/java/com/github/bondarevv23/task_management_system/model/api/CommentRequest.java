package com.github.bondarevv23.task_management_system.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {
    @NotNull
    @Schema(example = "123456789")
    private Integer taskId;

    @NotBlank
    @Schema(example = "some comment text")
    private String text;
}
