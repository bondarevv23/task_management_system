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
    /**
     * Return all tasks filtered by specification and paged.
     * @param specs filter specification
     * @param pageable pageable
     * @return page of tasks
     */
    Page<TaskDTO> findAll(Specification<Task> specs, Pageable pageable);

    /**
     * Return all paged tasks where passed user is performer.
     * @param performerUuid performer id of task
     * @param pageable pageable
     * @return page of tasks
     */
    Page<TaskDTO> findAllByPerformer(UUID performerUuid, Pageable pageable);

    /**
     * Return all tasks created by author
     * @param authorUuid author id
     * @param pageable pageable
     * @return page of tasks
     */
    Page<TaskDTO> findAllByAuthor(UUID authorUuid, Pageable pageable);

    /**
     * Create new task with author by task request
     * @param userId task author id
     * @param request task request
     * @return taskDTO of task
     */
    TaskDTO create(UUID userId, TaskRequest request);

    /**
     * Update task with corresponded ID by task request. Only author of a task can update a task.
     * @param userId task author ID
     * @param id task ID
     * @param request task request
     */
    void update(UUID userId, Integer id, TaskRequest request);

    /**
     * Delete task by ID. Only author of a task can delete a task.
     * @param userId author ID
     * @param id task ID
     */
    void delete(UUID userId, Integer id);

    /**
     * Change task status with corresponded ID. Only task performer can update its status.
     * @param userId performer ID
     * @param id task ID
     * @param status new task status
     */
    void changeStatus(UUID userId, Integer id, Status status);

    /**
     * Return task by ID
     * @param id task ID
     * @return taskDTO of corresponded task
     */
    TaskDTO findById(Integer id);
}
