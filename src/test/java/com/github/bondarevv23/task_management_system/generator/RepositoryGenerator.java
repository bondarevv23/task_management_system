package com.github.bondarevv23.task_management_system.generator;

import com.github.bondarevv23.task_management_system.model.Comment;
import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.User;
import com.github.bondarevv23.task_management_system.model.api.UserDTO;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.bondarevv23.task_management_system.TestConstants.*;
import static com.github.bondarevv23.task_management_system.TestConstants.TASK_PRIORITY;

@UtilityClass
public class RepositoryGenerator {
    public static User getAuthor() {
        return getUser1();
    }

    public static User getPerformer() {
        return getUser2();
    }

    public static UserDTO getDTOPerformer() {
        return userToUserDTO(getPerformer());
    }

    public static User getUser1() {
        return User.builder().id(UUID.fromString("344522b1-adcd-4fe0-8c90-ec8a86f93b78")).build();
    }

    public static User getUser2() {
        return User.builder().id(UUID.fromString("1505a117-363f-42fb-bfdf-636b8cff7657")).build();
    }

    public static User getUser3() {
        return User.builder().id(UUID.fromString("97a5b20d-ba09-4bef-9544-5bc1a7c6212e")).build();
    }

    private static UserDTO userToUserDTO(User user) {
        return new UserDTO(user.getId().toString());
    }

    public static Task getTask(int i) {
        LocalDateTime now = LocalDateTime.now();
        return Task.builder()
                .title(TASK_TITLE + i)
                .description(TASK_DESCRIPTION + i)
                .created(now)
                .modified(now)
                .author(getAuthor())
                .performer(getPerformer())
                .status(TASK_STATUS)
                .priority(TASK_PRIORITY)
                .build();
    }

    public static Comment getComment(Task task, int i) {
        LocalDateTime now = LocalDateTime.now();
        return Comment.builder()
                .author(getAuthor())
                .task(task)
                .text(COMMENT_TEXT + i)
                .created(now)
                .modified(now)
                .build();
    }

    public static Comment getCommentWithId(Task task, int i) {
        Comment comment = getComment(task, i);
        comment.setId(i);
        return comment;
    }

    public static Task getTaskWithId(int i) {
        Task task = getTask(i);
        task.setId(i);
        return task;
    }

    public static User getAuthorWithTasks(Task... tasks) {
        User author = getAuthor();
        author.setCreatedTasks(Arrays.stream(tasks).collect(Collectors.toList()));
        author.setTasksToPerform(Collections.emptyList());
        return author;
    }

    public static User getPerformerWithTasks(Task... tasks) {
        User performer = getPerformer();
        performer.setTasksToPerform(Arrays.stream(tasks).collect(Collectors.toList()));
        performer.setCreatedTasks(Collections.emptyList());
        return performer;
    }
}
