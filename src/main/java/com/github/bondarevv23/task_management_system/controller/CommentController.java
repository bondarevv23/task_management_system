package com.github.bondarevv23.task_management_system.controller;

import com.github.bondarevv23.task_management_system.controller.api.CommentApi;
import com.github.bondarevv23.task_management_system.model.api.CommentDTO;
import com.github.bondarevv23.task_management_system.model.api.CommentRequest;
import com.github.bondarevv23.task_management_system.service.interfaces.CommentService;
import com.github.bondarevv23.task_management_system.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentApi {
    private final CommentService service;

    @Override
    public ResponseEntity<CommentDTO> write(CommentRequest request) {
        UUID authorId = SecurityUtils.getUserId();
        return ResponseEntity.ok(service.write(authorId, request));
    }

    @Override
    public ResponseEntity<Void> update(Integer id, CommentRequest request) {
        UUID authorId = SecurityUtils.getUserId();
        service.update(authorId, id, request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CommentDTO> get(Integer id) {
        return ResponseEntity.ok(service.get(id));
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {
        UUID authorId = SecurityUtils.getUserId();
        service.delete(authorId, id);
        return ResponseEntity.ok().build();
    }
}
