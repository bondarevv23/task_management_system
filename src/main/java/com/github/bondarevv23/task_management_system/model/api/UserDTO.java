package com.github.bondarevv23.task_management_system.model.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @Schema(example = "5d814e66-9c1c-11ee-8c90-0242ac120002")
    private String id;
}
