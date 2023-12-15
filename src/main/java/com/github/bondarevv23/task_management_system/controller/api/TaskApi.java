package com.github.bondarevv23.task_management_system.controller.api;

import com.github.bondarevv23.task_management_system.model.Status;
import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.api.TaskDTO;
import com.github.bondarevv23.task_management_system.model.api.TaskRequest;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/tasks")
@Tag(name = "tasks")
public interface TaskApi {
    @Operation(summary = "returns all tasks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "list of all tasks",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TaskDTO.class))
                            )
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user"
            )})
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Page<TaskDTO>> findAll(@Filter Specification<Task> specs, Pageable pageable);

    @Operation(summary = "returns all tasks where user is performer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "list of tasks performed by user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TaskDTO.class))
                            )
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user"
            )
    })
    @GetMapping(
            value = "/my",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Page<TaskDTO>> findAllByPerformer(Pageable pageable);

    @Operation(summary = "returns all tasks owned by user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "list of tasks owned by user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TaskDTO.class))
                            )
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user"
            )
    })
    @GetMapping(
            value = "/my/created",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Page<TaskDTO>> findAllByAuthor(Pageable pageable);

    @Operation(summary = "returns task by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "corresponded task",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskDTO.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "unauthorized user"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "task with this id doesnt exist"
            )
    })
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<TaskDTO> findById(@PathVariable("id") Integer id);

    @Operation(summary = "create new task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "taskDTO of new created task",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskDTO.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "invalid request passed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "performer with passed id doesnt exist"
            )
    })
    @PostMapping(
            value = "/my",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<TaskDTO> create(@Valid @RequestBody TaskRequest request);

    @Operation(summary = "update an existing task by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "task successfully updated",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskDTO.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "invalid request passed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "user has no task with this id"
            )
    })
    @PutMapping(
            value = "/my/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> update(@PathVariable("id") Integer id, @Valid @RequestBody TaskRequest request);

    @Operation(summary = "delete an existing task by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "task successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "user has no task with this id"
            )
    })
    @DeleteMapping(
            value = "/my/{id}"
    )
    ResponseEntity<Void> delete(@PathVariable("id") Integer id);

    @Operation(summary = "the performer changes the task status")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "status successfully changed"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "unknown task status passed"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "performer has no task with this id"
            )
    })
    @PostMapping(
            value = "/change-status/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> changeStatus(@PathVariable("id") Integer id, @RequestBody Status status);
}
