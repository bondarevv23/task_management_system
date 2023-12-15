package com.github.bondarevv23.task_management_system.service.interfaces;

import com.github.bondarevv23.task_management_system.model.Status;
import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.api.TaskDTO;
import com.github.bondarevv23.task_management_system.model.api.TaskRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface TaskService {
    Page<TaskDTO> findAll(Specification<Task> specs, Pageable pageable);

    Page<TaskDTO> findAllByPerformer(UUID performerUuid, Pageable pageable);

    Page<TaskDTO> findAllByAuthor(UUID authorUuid, Pageable pageable);

    TaskDTO create(UUID userId, TaskRequest request);

    void update(UUID userId, Integer id, TaskRequest request);

    void delete(UUID userId, Integer id);

    void changeStatus(UUID userId, Integer id, Status status);

    TaskDTO findById(Integer id);
}
