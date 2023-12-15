package com.github.bondarevv23.task_management_system.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableTaskResponse {
    private List<TaskDTO> content;
}
