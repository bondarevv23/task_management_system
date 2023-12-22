package com.github.bondarevv23.task_management_system.generator;

import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.api.CommentRequest;
import com.github.bondarevv23.task_management_system.model.api.RefreshTokenRequest;
import com.github.bondarevv23.task_management_system.model.api.TaskRequest;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;
import com.github.bondarevv23.task_management_system.util.WrongData;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import static com.github.bondarevv23.task_management_system.TestConstants.*;
import static com.github.bondarevv23.task_management_system.generator.RepositoryGenerator.getDTOPerformer;

@UtilityClass
public class RequestGenerator {
    public static RefreshTokenRequest getRightRefreshToken() {
        return new RefreshTokenRequest("right-refresh-token");
    }

    public static RefreshTokenRequest getWrongRefreshToken() {
        return new RefreshTokenRequest("wrong-refresh-token");
    }

    public static UserCredentials getRightCredentials() {
        return user1Credentials();
    }

    public static UserCredentials getAuthorCredentials() {
        return user1Credentials();
    }

    public static UserCredentials getPerformerCredentials() {
        return user2Credentials();
    }

    public static UserCredentials getWrongCredentials() {
        return UserCredentials.builder()
                .email("some@email.com")
                .password("some-password")
                .build();
    }

    public static UserCredentials user1Credentials() {
        return UserCredentials.builder()
                .email("user1@test.com")
                .password("password")
                .build();
    }

    public static UserCredentials user2Credentials() {
        return UserCredentials.builder()
                .email("user2@test.com")
                .password("password")
                .build();
    }

    public static UserCredentials user3Credentials() {
        return UserCredentials.builder()
                .email("user3@test.com")
                .password("password")
                .build();
    }

    public static WrongData getWrongData() {
        return new WrongData("wrong-data");
    }

    public static String getInvalidBearerToken() {
        return "invalid-token";
    }

    public static TaskRequest getTaskRequest(int i) {
        return TaskRequest.builder()
                .title(TASK_TITLE + i)
                .description(TASK_DESCRIPTION + i)
                .performer(getDTOPerformer())
                .priority(TASK_PRIORITY)
                .build();
    }

    public static CommentRequest getCommentRequest(Task task, int i) {
        return CommentRequest.builder()
                .taskId(task.getId())
                .text(COMMENT_TEXT + (i + 1))
                .build();
    }

    public static Specification<Task> getTaskSpecification() {
        return (root, query, cb) -> cb.lessThan(root.get("id").as(Integer.class), 37);
    }

    public static Pageable getPageRequest() {
        return PageRequest.of(0, 10);
    }
}
