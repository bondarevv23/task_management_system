package com.github.bondarevv23.task_management_system.model.mapper;

import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.api.TaskDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDTO taskToTaskDTO(Task task);
}
