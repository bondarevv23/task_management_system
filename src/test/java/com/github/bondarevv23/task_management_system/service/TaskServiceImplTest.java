package com.github.bondarevv23.task_management_system.service;

import com.github.bondarevv23.task_management_system.exceptions.TaskNotFoundException;
import com.github.bondarevv23.task_management_system.exceptions.UserNotFoundException;
import com.github.bondarevv23.task_management_system.model.Status;
import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.User;
import com.github.bondarevv23.task_management_system.model.api.TaskDTO;
import com.github.bondarevv23.task_management_system.model.api.TaskRequest;
import com.github.bondarevv23.task_management_system.model.api.UserDTO;
import com.github.bondarevv23.task_management_system.model.mapper.TaskMapper;
import com.github.bondarevv23.task_management_system.repository.TaskRepository;
import com.github.bondarevv23.task_management_system.repository.UserRepository;
import com.github.bondarevv23.task_management_system.service.interfaces.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.stream.IntStream;

import static com.github.bondarevv23.task_management_system.generator.RepositoryGenerator.*;
import static com.github.bondarevv23.task_management_system.generator.RequestGenerator.*;
import static com.github.bondarevv23.task_management_system.generator.ResponseGenerator.getPageTask;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {
    private TaskService service;
    private TaskRepository taskRepository;
    private final TaskMapper mapper = Mappers.getMapper(TaskMapper.class);

    private final Task savedTask = getTaskWithId(1);

    @BeforeEach
    void init() {
        taskRepository = mock(TaskRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        service = new TaskServiceImpl(taskRepository, userRepository, mapper);
        doAnswer(res -> getPageTask()).when(taskRepository).findAll(any(Specification.class), any(Pageable.class));
        doAnswer(res -> getPageTask()).when(taskRepository).findAllByPerformer(any(User.class), any(Pageable.class));
        doAnswer(res -> getPageTask()).when(taskRepository).findAllByAuthor(any(User.class), any(Pageable.class));
        when(userRepository.findById(getAuthor().getId()))
                .thenReturn(Optional.of(getAuthorWithTasks(getTaskWithId(1), getTaskWithId(2))));
        when(userRepository.findById(getPerformer().getId()))
                .thenReturn(Optional.of(getPerformerWithTasks(getTaskWithId(1), getTaskWithId(2))));
        when(userRepository.findById(getUser3().getId())).thenReturn(Optional.empty());
        IntStream.range(1, 6).forEach(i -> {
            when(taskRepository.findById(i)).thenReturn(Optional.of(getTaskWithId(i)));
        });
        IntStream.range(-5, 0).forEach(i -> {
            when(taskRepository.findById(i)).thenReturn(Optional.empty());
        });
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
    }

    @Test
    void whenFindAllTasks_thenPageTasksReturns() {
        // given
        Specification<Task> specification = getTaskSpecification();
        Pageable page = getPageRequest();

        // when
        Page<TaskDTO> all = service.findAll(specification, page);

        // then
        verify(taskRepository, times(1)).findAll(specification, page);
    }

    @Test
    void whenFindAllByAuthor_thenPageTasksReturns() {
        // given
        Pageable page = getPageRequest();

        // when
        Page<TaskDTO> all = service.findAllByAuthor(getAuthor().getId(), page);

        // then
        verify(taskRepository, times(1)).findAllByAuthor(any(User.class), eq(page));
    }

    @Test
    void whenFindAllByPerformer_thenPageTasksReturns() {
        // given
        Pageable page = getPageRequest();

        // when
        Page<TaskDTO> all = service.findAllByPerformer(getPerformer().getId(), page);

        // then
        verify(taskRepository, times(1)).findAllByPerformer(any(User.class), eq(page));
    }

    @Test
    void whenGetExistingTaskById_thenTaskReturns() {
        // given
        int taskId = 2;

        // when
        TaskDTO task = service.findById(taskId);

        // then
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void whenGetTaskByInvalidId_thenTaskNotFoundExceptionThrows() {
        // given
        int invalidId = -3;

        // when

        // then
        assertThatThrownBy(() -> service.findById(invalidId)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void whenCreateNewTaskWithRightRequest_thenNewTaskReturns() {
        // given
        TaskRequest request = getTaskRequest(1);

        // when
        TaskDTO taskDTO = service.create(getAuthor().getId(), request);

        // then
        assertThat(taskDTO).isEqualTo(mapper.taskToTaskDTO(savedTask));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void whenCreateNewTaskWithUnknownPerformer_thenUserNotFoundExceptionThrows() {
        // given
        TaskRequest request = getTaskRequest(1);
        request.setPerformer(new UserDTO(getUser3().getId().toString()));

        // when

        // then
        assertThatThrownBy(() -> service.create(getAuthor().getId(), request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void whenUpdateTaskWithRightRequest_thenSaveFunctionCallsOnce() {
        // given
        int taskId = 1;
        TaskRequest request = getTaskRequest(taskId);

        // when
        service.update(getAuthor().getId(), taskId, request);

        // then
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void whenUserThatHaveNoTaskUpdatesIt_thenTaskNotFoundExceptionThrows() {
        // given
        int taskId = 3;
        TaskRequest request = getTaskRequest(taskId);

        // when

        // then
        assertThatThrownBy(() -> service.update(getAuthor().getId(), taskId, request))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void whenDeleteTaskByExistingId_thenFunctionDeleteCallsOnce() {
        // given
        int taskId = 2;

        // when
        service.delete(getAuthor().getId(), taskId);

        // then
        verify(taskRepository, times(1)).delete(any(Task.class));
    }

    @Test
    void whenDeleteTaskByUnknownId_thenTaskNotFoundExceptionThrows() {
        // given
        int taskId = -1;

        // when

        // then
        assertThatThrownBy(() -> service.delete(getAuthor().getId(), taskId))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void whenPerformerChangesTaskStatus_thenSaveFunctionCallsOnce() {
        // given
        int taskId = 1;
        Status newStatus = Status.FINISHED;

        // when
        service.changeStatus(getPerformer().getId(), taskId, newStatus);

        // then
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void whenNotPerformerChangesTaskStatus_thenTaskNotFoundExceptionThrows() {
        // given
        int taskId = 1;
        Status newStatus = Status.FINISHED;

        // when

        // then
        assertThatThrownBy(() -> service.changeStatus(getAuthor().getId(), taskId, newStatus))
                .isInstanceOf(TaskNotFoundException.class);
    }
}
