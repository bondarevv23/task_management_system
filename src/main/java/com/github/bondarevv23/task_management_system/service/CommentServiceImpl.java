package com.github.bondarevv23.task_management_system.service;

import com.github.bondarevv23.task_management_system.exceptions.CommentNotFoundException;
import com.github.bondarevv23.task_management_system.exceptions.InvalidDBStateException;
import com.github.bondarevv23.task_management_system.exceptions.TaskNotFoundException;
import com.github.bondarevv23.task_management_system.model.Comment;
import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.User;
import com.github.bondarevv23.task_management_system.model.api.CommentDTO;
import com.github.bondarevv23.task_management_system.model.api.CommentRequest;
import com.github.bondarevv23.task_management_system.model.mapper.TaskMapper;
import com.github.bondarevv23.task_management_system.repository.CommentRepository;
import com.github.bondarevv23.task_management_system.repository.TaskRepository;
import com.github.bondarevv23.task_management_system.repository.UserRepository;
import com.github.bondarevv23.task_management_system.service.interfaces.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper mapper;

    @Override
    public CommentDTO write(UUID authorId, CommentRequest request) {
        User author = getUser(authorId);
        if (!taskRepository.existsById(request.getTaskId())) {
            throw new TaskNotFoundException(String.format("there is no task with id %d", request.getTaskId()));
        }
        LocalDateTime now = LocalDateTime.now();
        Comment comment = Comment.builder()
                .author(author)
                .task(Task.builder().id(request.getTaskId()).build())
                .text(request.getText())
                .created(now)
                .modified(now)
                .build();
        return mapper.commentToCommentDTO(commentRepository.save(comment));
    }

    @Override
    public void delete(UUID authorId, Integer id) {
        Comment comment = getComment(id);
        checkIfUserWroteThisComment(comment, authorId);
        commentRepository.delete(comment);
    }

    @Override
    public void update(UUID authorId, Integer id, CommentRequest request) {
        Comment comment = getComment(id);
        checkIfUserWroteThisComment(comment, authorId);
        comment.setText(request.getText());
        comment.setModified(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Override
    public CommentDTO get(Integer id) {
        return mapper.commentToCommentDTO(getComment(id));
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new InvalidDBStateException(String.format("server cant find user by id %s", userId))
        );
    }

    private Comment getComment(Integer id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new CommentNotFoundException(String.format("comment with id %d doesnt exist", id))
        );
    }

    private void checkIfUserWroteThisComment(Comment comment, UUID authorId) {
        if (!Objects.equals(comment.getAuthor().getId(), authorId)) {
            throw new CommentNotFoundException(
                    String.format("user with id %s does not have comment with id %d",
                            authorId.toString(), comment.getId())
            );
        }
    }
}
