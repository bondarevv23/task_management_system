package com.github.bondarevv23.task_management_system.model.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.bondarevv23.task_management_system.model.Priority;
import com.github.bondarevv23.task_management_system.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsExclude;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDTO {
    @Schema(example = "123456789")
    private Integer id;

    @Schema(example = "task title")
    private String title;

    @Schema(example = "task description")
    private String description;

    private UserDTO author;

    private UserDTO performer;

    @JsonProperty("task_status")
    private Status status;

    private Priority priority;

    private List<CommentDTO> comments;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modified;
}
