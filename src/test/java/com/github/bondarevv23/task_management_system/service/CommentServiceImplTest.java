package com.github.bondarevv23.task_management_system.service;

import com.github.bondarevv23.task_management_system.exceptions.CommentNotFoundException;
import com.github.bondarevv23.task_management_system.exceptions.TaskNotFoundException;
import com.github.bondarevv23.task_management_system.model.Comment;
import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.api.CommentDTO;
import com.github.bondarevv23.task_management_system.model.api.CommentRequest;
import com.github.bondarevv23.task_management_system.model.mapper.TaskMapper;
import com.github.bondarevv23.task_management_system.repository.CommentRepository;
import com.github.bondarevv23.task_management_system.repository.TaskRepository;
import com.github.bondarevv23.task_management_system.repository.UserRepository;
import com.github.bondarevv23.task_management_system.service.interfaces.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Optional;
import java.util.stream.IntStream;

import static com.github.bondarevv23.task_management_system.generator.RepositoryGenerator.*;
import static com.github.bondarevv23.task_management_system.generator.RequestGenerator.getCommentRequest;
import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceImplTest {
    private CommentRepository commentRepository;
    private TaskRepository taskRepository;
    private CommentService service;

    private final Task task = getTaskWithId(1);

    @BeforeEach
    void init() {
        commentRepository = mock(CommentRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        taskRepository = mock(TaskRepository.class);
        service = new CommentServiceImpl(
                commentRepository,
                userRepository,
                taskRepository,
                Mappers.getMapper(TaskMapper.class)
        );
        IntStream.range(1, 6).forEach(i -> {
            doNothing().when(commentRepository).delete(getCommentWithId(task, i));
            Comment modifiedComment = getCommentWithId(task, i);
            modifiedComment.setText(getCommentRequest(task, i).getText());
            when(commentRepository.findById(i)).thenReturn(Optional.of(getCommentWithId(task, i)));
            when(taskRepository.existsById(i)).thenReturn(true);
        });
        IntStream.range(-5, 0).forEach(i -> {
            when(commentRepository.findById(i)).thenReturn(Optional.empty());
            when(taskRepository.existsById(i)).thenReturn(false);
        });
        when(userRepository.findById(getAuthor().getId())).thenReturn(Optional.of(getAuthor()));
        doAnswer(req -> req.getArgument(0)).when(commentRepository).save(any(Comment.class));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
    }

    @Test
    void whenWriteCommentWithRightRequest_thenCommentReturns() {
        // given
        CommentRequest request = getCommentRequest(task, 1);

        // when
        CommentDTO comment = service.write(getAuthor().getId(), request);

        // then
        assertThat(comment).isEqualToComparingOnlyGivenFields(request, "text");
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void whenWriteCommentWithUnknownTaskId_thenTaskNotFoundThrows() {
        // given
        CommentRequest request = getCommentRequest(task, 2);
        request.setTaskId(-7);

        // when

        // then
        assertThatThrownBy(() -> service.write(getAuthor().getId(), request)).isInstanceOf(TaskNotFoundException.class);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void whenDeleteExistingCommentById_thenDeleteFunctionCallsOnce() {
        // given
        int commentId = 3;

        // when
        service.delete(getAuthor().getId(), commentId);

        // then
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }

    @Test
    void whenDeleteCommentByUnknownId_thenCommentNoFoundExceptionThrows() {
        // given
        int commentId = -4;

        // when

        // then
        assertThatThrownBy(() -> service.delete(getAuthor().getId(), commentId))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    void whenUpdateCommentWithRightRequest_thenSaveFunctionCallsOnce() {
        // given
        int commentId = 1;
        CommentRequest request = getCommentRequest(task, 1);

        // when
        service.update(getAuthor().getId(), commentId, request);

        // then
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void whenUpdateWithUnknownCommentId_thenCommentNotFoundExceptionThrows() {
        // given
        int unknownId = -3;
        CommentRequest request = getCommentRequest(task, 2);

        // when

        // then
        assertThatThrownBy(() -> service.update(getAuthor().getId(), unknownId, request))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    void whenGetCommentWithRightId_thenCommentReturns() {
        // given
        int commentId = 1;

        // when
        CommentDTO commentDTO = service.get(commentId);

        // then
        assertThat(commentDTO.getTaskId()).isEqualTo(task.getId());
        assertThat(commentDTO.getText()).isEqualTo(getComment(task, commentId).getText());
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void whenGetCommentByInvalidId_thenCommentNotFoundExceptionThrows() {
        // given
        int invalidId = -2;

        // when

        // then
        assertThatThrownBy(() -> service.get(invalidId)).isInstanceOf(CommentNotFoundException.class);
        verify(commentRepository, times(1)).findById(invalidId);
    }
}
