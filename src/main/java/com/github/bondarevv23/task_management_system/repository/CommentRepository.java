package com.github.bondarevv23.task_management_system.repository;

import com.github.bondarevv23.task_management_system.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
