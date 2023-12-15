package com.github.bondarevv23.task_management_system.repository;

import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {
    Page<Task> findAllByPerformer(User performer, Pageable pageable);

    Page<Task> findAllByAuthor(User author, Pageable pageable);
}
