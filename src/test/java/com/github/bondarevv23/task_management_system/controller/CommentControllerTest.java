package com.github.bondarevv23.task_management_system.controller;

import com.github.bondarevv23.task_management_system.model.*;
import com.github.bondarevv23.task_management_system.model.api.CommentDTO;
import com.github.bondarevv23.task_management_system.model.api.CommentRequest;
import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;
import com.github.bondarevv23.task_management_system.repository.CommentRepository;
import com.github.bondarevv23.task_management_system.repository.TaskRepository;
import com.github.bondarevv23.task_management_system.util.IntegrationEnvironment;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

import static com.github.bondarevv23.task_management_system.generator.RepositoryGenerator.*;
import static com.github.bondarevv23.task_management_system.generator.RequestGenerator.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest extends IntegrationEnvironment {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CommentRepository commentRepository;

    private String accessToken;

    private Task task;

    @BeforeAll
    void initTask() {
        task = taskRepository.save(getTask(1));
    }

    @BeforeEach
    void initToken() {
        accessToken = getAccessToken(getAuthorCredentials());
    }

    @Test
    void whenWriteNewCommentWithRightRequest_thenOKAndCommentAddsToDB() {
        // given
        CommentRequest request = getCommentRequest(task, 1);

        // when
        ResponseEntity<CommentDTO> response = client.post()
                .uri("api/v1/comments")
                .headers(h -> h.setBearerAuth(accessToken))
                .bodyValue(request)
                .retrieve()
                .toEntity(CommentDTO.class).block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualToComparingOnlyGivenFields(request,
                "text", "taskId");
        assertThat(commentRepository.findById(response.getBody().getId()).isPresent()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    void whenWriteNeCommentWithInvalidRequest_thenBadRequest(Object invalidRequest) {
        // given

        // when
        ResponseEntity<CommentDTO> response = client.post()
                .uri("api/v1/comments")
                .headers(h -> h.setBearerAuth(accessToken))
                .bodyValue(invalidRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(CommentDTO.class).block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenWriteCommentWithInvalidBearerToken_thenUnauthorized() {
        // given
        String invalidBearerToken = "token";
        CommentRequest request = getCommentRequest(task, 3);

        // when
        ResponseEntity<CommentDTO> response = client.post()
                .uri("api/v1/comments")
                .headers(h -> h.setBearerAuth(invalidBearerToken))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(CommentDTO.class).block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenWriteCommentWithUndefinedTaskId_thenNotFound() {
        // given
        CommentRequest request = getCommentRequest(task, 3);
        request.setTaskId(-1);

        // when
        ResponseEntity<CommentDTO> response = client.post()
                .uri("api/v1/comments")
                .headers(h -> h.setBearerAuth(accessToken))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(CommentDTO.class).block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenUpdateCommentWithRightRequest_thenOKAndCommentUpdates() {
        // given
        Comment comment = getSavedComment(1);
        CommentRequest updateRequest = getCommentRequest(task, 2);

        // when
        ResponseEntity<Void> response = client.put()
                .uri("api/v1/comments/{id}", comment.getId())
                .headers(h -> h.setBearerAuth(accessToken))
                .bodyValue(updateRequest)
                .retrieve()
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Comment storredComment = commentRepository.findById(comment.getId()).orElseThrow();
        assertThat(storredComment.getText()).isEqualTo(updateRequest.getText());
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    void whenUpdateCommentWithInvalidRequest_thenBadRequest(Object request) {
        // given
        Comment comment = getSavedComment(1);

        // when
        ResponseEntity<Void> response = client.put()
                .uri("api/v1/comments/{id}", comment.getId())
                .headers(h -> h.setBearerAuth(accessToken))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenUpdateCommentWithInvalidBearerToken_thenUnauthorized() {
        // given
        Comment comment = getSavedComment(7);
        CommentRequest request = getCommentRequest(task, 11);
        String invalidToken = "token";

        // when
        ResponseEntity<Void> response = client.put()
                .uri("api/v1/comments/{id}", comment.getId())
                .headers(h -> h.setBearerAuth(invalidToken))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenUpdateCommentWithUndefinedId_thenNotFound() {
        // given
        Comment comment = getSavedComment(7);
        CommentRequest request = getCommentRequest(task, 11);

        // when
        ResponseEntity<Void> response = client.put()
                .uri("api/v1/comments/{id}", -1)
                .headers(h -> h.setBearerAuth(accessToken))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenGetCommentByExistingId_thenOKAndCommentReturns() {
        // given
        Comment comment = getSavedComment(1);

        // when
        ResponseEntity<CommentDTO> response = client.get()
                .uri("api/v1/comments/{id}", comment.getId())
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(CommentDTO.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getText()).isEqualTo(comment.getText());
        assertThat(response.getBody().getTaskId()).isEqualTo(comment.getTask().getId());
    }

    @Test
    void whenGetCommentWithInvalidBearerToken_thenUnauthorized() {
        // given
        Comment comment = getSavedComment(1);
        String invalidToken = "token";

        // when
        ResponseEntity<CommentDTO> response = client.get()
                .uri("api/v1/comments/{id}", comment.getId())
                .headers(h -> h.setBearerAuth(invalidToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(CommentDTO.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenGetCommentWithUnknownId_thenNotFound() {
        // given
        Integer unknownId = -1;

        // when
        ResponseEntity<CommentDTO> response = client.get()
                .uri("api/v1/comments/{id}", unknownId)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(CommentDTO.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenDeleteCommentByExistingId_thenOKAndCommentRemoves() {
        // given
        Comment comment = getSavedComment(1);

        // when
        ResponseEntity<Void> response = client.delete()
                .uri("api/v1/comments/{id}", comment.getId())
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(commentRepository.findById(comment.getId()).isEmpty()).isTrue();
    }

    @Test
    void whenDeleteCommentWithInvalidBearerToken_thenUnauthorized() {
        // given
        Comment comment = getSavedComment(1);
        String invalidToken = "token";

        // when
        ResponseEntity<Void> response = client.delete()
                .uri("api/v1/comments/{id}", comment.getId())
                .headers(h -> h.setBearerAuth(invalidToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenDeleteCommentWithUnknownId_thenNotFound() {
        // given

        // when
        ResponseEntity<Void> response = client.delete()
                .uri("api/v1/comments/{id}", -1)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenNotAuthorTriesToDeleteComment_thenNotFound() {
        // given
        Comment comment = getSavedComment(10);
        UserCredentials credentials = UserCredentials.builder()
                .email("user2@test.com")
                .password("password")
                .build();
        String notAuthorAccessToken = client.post()
                .uri("api/v1/users/get-token")
                .bodyValue(credentials)
                .retrieve()
                .toEntity(TokenResponse.class)
                .block().getBody().getAccessToken();

        // when
        ResponseEntity<Void> response = client.delete()
                .uri("api/v1/comments/{id}", comment.getId())
                .headers(h -> h.setBearerAuth(notAuthorAccessToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Stream<Arguments> invalidRequests() {
        return Stream.of(
                Arguments.of(CommentRequest.builder()
                        .text("some text")
                        .build()),
                Arguments.of(CommentRequest.builder()
                        .taskId(task.getId())
                        .build()),
                Arguments.of(CommentRequest.builder()
                        .taskId(task.getId())
                        .text("          ")
                        .build()),
                Arguments.of(getWrongData())
        );
    }

    private Comment getSavedComment(int i) {
        return commentRepository.save(getComment(task, i));
    }
}
