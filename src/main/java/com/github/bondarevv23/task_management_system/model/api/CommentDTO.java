package com.github.bondarevv23.task_management_system.model.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    @Schema(example = "123456789")
    private Integer id;

    @Schema(example = "123456789")
    private Integer taskId;

    @Schema(example = "example of comment to some task")
    private String text;

    private UserDTO author;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modified;
}
