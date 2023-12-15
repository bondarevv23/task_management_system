package com.github.bondarevv23.task_management_system.service.interfaces;

import com.github.bondarevv23.task_management_system.model.api.CommentDTO;
import com.github.bondarevv23.task_management_system.model.api.CommentRequest;

import java.util.UUID;

public interface CommentService {
    CommentDTO write(UUID authorId, CommentRequest request);

    void delete(UUID authorId, Integer id);

    void update(UUID authorId, Integer id, CommentRequest request);

    CommentDTO get(Integer id);
}
