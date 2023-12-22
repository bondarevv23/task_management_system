package com.github.bondarevv23.task_management_system.controller;

import com.github.bondarevv23.task_management_system.controller.api.TaskApi;
import com.github.bondarevv23.task_management_system.model.Status;
import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.api.TaskDTO;
import com.github.bondarevv23.task_management_system.model.api.TaskRequest;
import com.github.bondarevv23.task_management_system.service.interfaces.TaskService;
import com.github.bondarevv23.task_management_system.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskController implements TaskApi {
    private final TaskService service;

    @Override
    public ResponseEntity<Page<TaskDTO>> findAll(Specification<Task> specs, Pageable pageable) {
        return ResponseEntity.ok(service.findAll(specs, pageable));
    }

    @Override
    public ResponseEntity<Page<TaskDTO>> findAllByPerformer(Pageable pageable) {
        UUID userId = SecurityUtils.getUserId();
        return ResponseEntity.ok(service.findAllByPerformer(userId, pageable));
    }

    @Override
    public ResponseEntity<Page<TaskDTO>> findAllByAuthor(Pageable pageable) {
        UUID userId = SecurityUtils.getUserId();
        return ResponseEntity.ok(service.findAllByAuthor(userId, pageable));
    }

    @Override
    public ResponseEntity<TaskDTO> findById(Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    public ResponseEntity<TaskDTO> create(TaskRequest request) {
        UUID userId = SecurityUtils.getUserId();
        return ResponseEntity.ok(service.create(userId, request));
    }

    @Override
    public ResponseEntity<Void> update(Integer id, TaskRequest request) {
        UUID userId = SecurityUtils.getUserId();
        service.update(userId, id, request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {
        UUID userId = SecurityUtils.getUserId();
        service.delete(userId, id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> changeStatus(Integer id, Status status) {
        UUID userId = SecurityUtils.getUserId();
        service.changeStatus(userId, id, status);
        return ResponseEntity.ok().build();
    }
}
