package com.github.bondarevv23.task_management_system.service;

import com.github.bondarevv23.task_management_system.exceptions.InvalidDBStateException;
import com.github.bondarevv23.task_management_system.exceptions.TaskNotFoundException;
import com.github.bondarevv23.task_management_system.exceptions.UserNotFoundException;
import com.github.bondarevv23.task_management_system.model.Status;
import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.User;
import com.github.bondarevv23.task_management_system.model.api.TaskDTO;
import com.github.bondarevv23.task_management_system.model.api.TaskRequest;
import com.github.bondarevv23.task_management_system.model.mapper.TaskMapper;
import com.github.bondarevv23.task_management_system.repository.TaskRepository;
import com.github.bondarevv23.task_management_system.repository.UserRepository;
import com.github.bondarevv23.task_management_system.service.interfaces.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper mapper;

    @Override
    public Page<TaskDTO> findAll(Specification<Task> specs, Pageable pageable) {
        return taskRepository.findAll(specs, pageable).map(mapper::taskToTaskDTO);
    }

    @Override
    public Page<TaskDTO> findAllByPerformer(UUID performerUuid, Pageable pageable) {
        User performer = getUserFromRequest(performerUuid);
        return taskRepository.findAllByPerformer(performer, pageable).map(mapper::taskToTaskDTO);
    }

    @Override
    public Page<TaskDTO> findAllByAuthor(UUID authorUuid, Pageable pageable) {
        User author = getUser(authorUuid);
        return taskRepository.findAllByAuthor(author, pageable).map(mapper::taskToTaskDTO);
    }

    @Override
    public TaskDTO create(UUID userId, TaskRequest request) {
        User author = getUser(userId);
        Task task = createTaskFromRequest(request);
        task.setAuthor(author);
        return mapper.taskToTaskDTO(taskRepository.save(task));
    }

    @Override
    public void update(UUID userId, Integer id, TaskRequest request) {
        User author = getUser(userId);
        Task task = findCreatedByUserTaskById(author, id);
        updateTaskByRequest(task, request);
        taskRepository.save(task);
    }

    @Override
    public void delete(UUID userId, Integer id) {
        User author = getUser(userId);
        Task task = findCreatedByUserTaskById(author, id);
        taskRepository.delete(task);
    }

    @Override
    public void changeStatus(UUID userId, Integer id, Status status) {
        User performer = getUser(userId);
        Task task = findTaskToPerformByUserById(performer, id);
        task.setStatus(status);
        taskRepository.save(task);
    }

    @Override
    public TaskDTO findById(Integer id) {
        return mapper.taskToTaskDTO(taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException(String.format("task with id %d doesnt exist", id))
        ));
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new InvalidDBStateException(String.format("server cant find user by id %s", userId))
        );
    }

    private User getUserFromRequest(UUID performerUuid) {
        return userRepository.findById(performerUuid).orElseThrow(
                () -> new UserNotFoundException(String.format("user with id %s doesnt exist", performerUuid))
        );
    }

    private Task findCreatedByUserTaskById(User user, Integer id) {
        return findTaskFromListById(user.getCreatedTasks(), id);
    }

    private Task findTaskToPerformByUserById(User user, Integer id) {
        return findTaskFromListById(user.getTasksToPerform(), id);
    }

    private Task findTaskFromListById(List<Task> tasks, Integer id) {
        return tasks.stream()
                .filter(task -> Objects.equals(id, task.getId()))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(String.format("task with id %d doesnt exist", id)));
    }

    private Task createTaskFromRequest(TaskRequest request) {
        User performer = getUserFromRequest(UUID.fromString(request.getPerformer().getId()));
        LocalDateTime now = LocalDateTime.now();
        return Task.builder()
                .status(Status.WAITING)
                .title(request.getTitle())
                .description(request.getDescription())
                .performer(performer)
                .priority(request.getPriority())
                .created(now)
                .modified(now)
                .build();
    }

    private void updateTaskByRequest(Task task, TaskRequest request) {
        User performer = getUserFromRequest(UUID.fromString(request.getPerformer().getId()));
        task.setPerformer(performer);
        task.setPriority(request.getPriority());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setModified(LocalDateTime.now());
    }
}
