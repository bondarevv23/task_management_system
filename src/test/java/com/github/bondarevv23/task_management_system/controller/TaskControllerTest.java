package com.github.bondarevv23.task_management_system.controller;

import com.github.bondarevv23.task_management_system.model.Priority;
import com.github.bondarevv23.task_management_system.model.Status;
import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.api.PageableTaskResponse;
import com.github.bondarevv23.task_management_system.model.api.TaskDTO;
import com.github.bondarevv23.task_management_system.model.api.TaskRequest;
import com.github.bondarevv23.task_management_system.model.api.UserDTO;
import com.github.bondarevv23.task_management_system.repository.TaskRepository;
import com.github.bondarevv23.task_management_system.util.IntegrationEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.bondarevv23.task_management_system.generator.RepositoryGenerator.getDTOPerformer;
import static com.github.bondarevv23.task_management_system.generator.RepositoryGenerator.getTask;
import static com.github.bondarevv23.task_management_system.generator.RequestGenerator.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest extends IntegrationEnvironment {
    @Autowired
    private TaskRepository taskRepository;

    private String authorAccessToken;
    private String performerAccessToken;
    private String invalidToken;

    @BeforeEach
    void init() {
        authorAccessToken = getAccessToken(getAuthorCredentials());
        performerAccessToken = getAccessToken(getPerformerCredentials());
        invalidToken = getInvalidBearerToken();
    }

    @BeforeEach
    void clean() {
        taskRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("filters")
    void whenFindAllWithPageAndFilter_thenPageWithFilteredTasksReturns(int count, int lowerBound, int upperBound) {
        // given
        IntStream.range(1, count + 1).forEach(this::getSavedTask);
        int page = 0;
        int size = 30;
        String filter = String.format("id > %d and id < %d", lowerBound, upperBound);

        // when
        ResponseEntity<PageableTaskResponse> response = client.get()
                .uri(builder -> builder
                        .path("api/v1/tasks")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("filter", filter)
                        .build())
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .retrieve()
                .toEntity(PageableTaskResponse.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<TaskDTO> responseTasks = response.getBody().getContent();
        assertThat(responseTasks.size()).isLessThanOrEqualTo(size);
        assertThat(responseTasks.stream().map(TaskDTO::getId).max(Comparator.naturalOrder()).orElse(Integer.MIN_VALUE))
                .isLessThanOrEqualTo(upperBound);
        assertThat(responseTasks.stream().map(TaskDTO::getId).min(Comparator.naturalOrder()).orElse(Integer.MAX_VALUE))
                .isGreaterThanOrEqualTo(lowerBound);
    }

    private Stream<Arguments> filters() {
        return Stream.generate(() -> ThreadLocalRandom.current().nextInt(100, 500))
                .map(i -> {
                    int lowerBound = ThreadLocalRandom.current().nextInt(0, i / 2);
                    int upperBound = ThreadLocalRandom.current().nextInt(lowerBound, i);
                    return Arguments.of(i, lowerBound, upperBound);
                }).limit(10);
    }

    @Test
    void whenFindAllWithInvalidBearerToken_thenUnauthorized() {
        // given

        // when
        ResponseEntity<PageableTaskResponse> response = client.get()
                .uri("api/v1/tasks")
                .headers(h -> h.setBearerAuth(invalidToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(PageableTaskResponse.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10, 100})
    void whenFindAllTaskByPerformerWithSortAndPage_thenReturnsAllTaskSortedAndPaged(int count) {
        // given
        List<Task> tasks = IntStream.range(1, count + 1).mapToObj(this::getSavedTask).toList();
        tasks.forEach(task -> task.setComments(Collections.emptyList()));
        int size = 17;

        // when
        ResponseEntity<PageableTaskResponse> response = client.get()
                .uri("api/v1/tasks/my/created?page=0&size=17&sort=id,desc")
                .headers(h -> h.setBearerAuth(performerAccessToken))
                .retrieve()
                .toEntity(PageableTaskResponse.class)
                .block();

        // then
        assertThat(response).isNotNull();
        List<TaskDTO> responseTasks = response.getBody().getContent();
        assertThat(responseTasks.size()).isLessThanOrEqualTo(size);
        assertThat(responseTasks).isSortedAccordingTo(Comparator.comparingInt(TaskDTO::getId).reversed());
    }

    @Test
    void whenFindAllTaskByPerformerWithInvalidBearerToken_thenUnauthorized() {
        // given
        IntStream.range(1, 5).forEach(this::getSavedTask);

        // when
        ResponseEntity<PageableTaskResponse> response = client.get()
                .uri("api/v1/tasks/my/created")
                .headers(h -> h.setBearerAuth(invalidToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(PageableTaskResponse.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10, 100})
    void whenFindAllTaskByAuthorWithFilterAndPage_thenReturnsAllTaskSortedAndPaged(int count) {
        // given
        List<Task> tasks = IntStream.range(1, count + 1).mapToObj(this::getSavedTask).toList();
        tasks.forEach(task -> task.setComments(Collections.emptyList()));
        int size = 10;

        // when
        ResponseEntity<PageableTaskResponse> response = client.get()
                .uri("api/v1/tasks/my/created?page=0&size=10&sort=id,desc")
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .retrieve()
                .toEntity(PageableTaskResponse.class)
                .block();

        // then
        assertThat(response).isNotNull();
        List<TaskDTO> responseTasks = response.getBody().getContent();
        assertThat(responseTasks.size()).isLessThanOrEqualTo(size);
        assertThat(responseTasks).isSortedAccordingTo(Comparator.comparingInt(TaskDTO::getId).reversed());
    }

    @Test
    void whenFindAllTaskByAuthorWithInvalidBearerToken_thenUnauthorized() {
        // given
        IntStream.range(1, 5).forEach(this::getSavedTask);

        // when
        ResponseEntity<PageableTaskResponse> response = client.get()
                .uri("api/v1/tasks/my/created")
                .headers(h -> h.setBearerAuth(invalidToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(PageableTaskResponse.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenFindTaskByRightId_thenTaskReturns() {
        // given
        Task task = getSavedTask(1);

        // when
        ResponseEntity<TaskDTO> response = client.get()
                .uri("api/v1/tasks/{id}", task.getId())
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .retrieve()
                .toEntity(TaskDTO.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualToComparingOnlyGivenFields(task,
                "id", "title", "description", "status", "priority");
    }

    @Test
    void whenFindTaskByIdWithInvalidBearerToken_thenUnauthorized() {
        // given
        Task task = getSavedTask(1);

        // when
        ResponseEntity<TaskDTO> response = client.get()
                .uri("api/v1/tasks/{id}", task.getId())
                .headers(h -> h.setBearerAuth(invalidToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TaskDTO.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenFindTaskByUnknownId_thenNotFound() {
        // given

        // when
        ResponseEntity<TaskDTO> response = client.get()
                .uri("api/v1/tasks/{id}", -1)
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TaskDTO.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenCreateTaskWithRightRequest_thenTaskCreated() {
        // given
        TaskRequest request = getTaskRequest(1);

        // when
        ResponseEntity<TaskDTO> response = client.post()
                .uri("api/v1/tasks/my")
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .bodyValue(request)
                .retrieve()
                .toEntity(TaskDTO.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(taskRepository.findById(response.getBody().getId()).isPresent()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    void whenCreateTaskByInvalidRequest_thenBadRequest(Object request) {
        // given

        // when
        ResponseEntity<TaskDTO> response = client.post()
                .uri("api/v1/tasks/my")
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TaskDTO.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenCreateTaskWithInvalidBearerToken_thenUnauthorized() {
        // given
        TaskRequest request = getTaskRequest(2);

        // when
        ResponseEntity<TaskDTO> response = client.post()
                .uri("api/v1/tasks/my")
                .headers(h -> h.setBearerAuth(invalidToken))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TaskDTO.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenCreateTaskWithUnknownPerformerId_thenNoFound() {
        // given
        TaskRequest request = getTaskRequest(3);
        request.setPerformer(new UserDTO(UUID.randomUUID().toString()));

        // when
        ResponseEntity<TaskDTO> response = client.post()
                .uri("api/v1/tasks/my")
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TaskDTO.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenUpdateTaskWithRightRequest_thenOKAndTaskUpdates() {
        // given
        Task task = getSavedTask(1);
        TaskRequest request = getTaskRequest(2);

        // when
        ResponseEntity<Void> response = client.put()
                .uri("api/v1/tasks/my/{id}", task.getId())
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .bodyValue(request)
                .retrieve()
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Task storedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(storedTask.getTitle()).isEqualTo(request.getTitle());
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    void whenUpdateTaskWithInvalidRequest_thenBadRequest(Object invalidRequest) {
        // given
        Task task = getSavedTask(7);

        // when
        ResponseEntity<Void> response = client.put()
                .uri("api/v1/tasks/my/{id}", task.getId())
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .bodyValue(invalidRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenUpdateTaskWithInvalidBearerToken_thenUnauthorized() {
        // given
        Task task = getSavedTask(1);
        TaskRequest request = getTaskRequest(2);

        // when
        ResponseEntity<Void> response = client.put()
                .uri("api/v1/tasks/my/{id}", task.getId())
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
    void whenUpdateTaskWithUnknownId_thenNotFound() {
        // given
        Integer unknownId = -10;
        TaskRequest request = getTaskRequest(1);

        // when
        ResponseEntity<Void> response = client.put()
                .uri("api/v1/tasks/my/{id}", unknownId)
                .headers(h -> h.setBearerAuth(authorAccessToken))
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
    void whenOwnerDeletesTaskByRightId_thenOKAndTaskRemoves() {
        // given
        Task task = getSavedTask(1);

        // when
        ResponseEntity<Void> response = client.delete()
                .uri("api/v1/tasks/my/{id}", task.getId())
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .retrieve()
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(taskRepository.findById(task.getId()).isEmpty()).isTrue();
    }

    @Test
    void whenDeleteTaskWithInvalidBearerToken_thenUnauthorized() {
        // given
        Task task = getSavedTask(1);

        // when
        ResponseEntity<Void> response = client.delete()
                .uri("api/v1/tasks/my/{id}", task.getId())
                .headers(h -> h.setBearerAuth(invalidToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(taskRepository.existsById(task.getId())).isTrue();
    }

    @Test
    void whenDeleteTaskByUnknownId_thenNotFound() {
        // given
        Integer unknownId = -17;

        // when
        ResponseEntity<Void> response = client.delete()
                .uri("api/v1/tasks/my/{id}", unknownId)
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenPerformerChangesStatusOfTask_OKAndStatusChanges() {
        // given
        Task task = getSavedTask(11);
        Status newStatus = Status.FINISHED;

        // when
        ResponseEntity<Void> response = client.post()
                .uri("api/v1/tasks/change-status/{id}", task.getId())
                .headers(h -> h.setBearerAuth(performerAccessToken))
                .bodyValue(newStatus)
                .retrieve()
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(taskRepository.findById(task.getId()).orElseThrow().getStatus()).isEqualTo(newStatus);
    }

    @Test
    void whenNotPerformerTriesToChangeStatus_thenNotFound() {
        // given
        Task task = getSavedTask(11);
        Status newStatus = Status.FINISHED;

        // when
        ResponseEntity<Void> response = client.post()
                .uri("api/v1/tasks/change-status/{id}", task.getId())
                .headers(h -> h.setBearerAuth(authorAccessToken))
                .bodyValue(newStatus)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenChangeStatusWithInvalidBearerToken_thenUnauthorized() {
        // given
        Task task = getSavedTask(8);
        Status newStatus = Status.FINISHED;

        // when
        ResponseEntity<Void> response = client.post()
                .uri("api/v1/tasks/change-status/{id}", task.getId())
                .headers(h -> h.setBearerAuth(invalidToken))
                .bodyValue(newStatus)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(Void.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private Task getSavedTask(int i) {
        return taskRepository.saveAndFlush(getTask(i));
    }

    private Stream<Arguments> invalidRequests() {
        return Stream.of(
                Arguments.of(TaskRequest.builder()
                        .title("   ")
                        .description("description")
                        .performer(getDTOPerformer())
                        .priority(Priority.MIDDLE)
                        .build()),
                Arguments.of(TaskRequest.builder()
                        .description("description")
                        .performer(getDTOPerformer())
                        .priority(Priority.MIDDLE)
                        .build()),
                Arguments.of(TaskRequest.builder()
                        .title("title".repeat(100))
                        .description("description")
                        .performer(getDTOPerformer())
                        .priority(Priority.MIDDLE)
                        .build()),
                Arguments.of(TaskRequest.builder()
                        .title("title")
                        .performer(getDTOPerformer())
                        .priority(Priority.MIDDLE)
                        .build()),
                Arguments.of(TaskRequest.builder()
                        .title("title")
                        .description("    ")
                        .performer(getDTOPerformer())
                        .priority(Priority.MIDDLE)
                        .build()),
                Arguments.of(TaskRequest.builder()
                        .title("title")
                        .description("description")
                        .priority(Priority.MIDDLE)
                        .build()),
                Arguments.of(TaskRequest.builder()
                        .title("title")
                        .description("description")
                        .performer(getDTOPerformer())
                        .build()),
                Arguments.of(getWrongData())
        );
    }
}
