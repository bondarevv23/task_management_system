package com.github.bondarevv23.task_management_system.controller.api;

import com.github.bondarevv23.task_management_system.model.api.ExceptionResponse;
import com.github.bondarevv23.task_management_system.model.Status;
import com.github.bondarevv23.task_management_system.model.Task;
import com.github.bondarevv23.task_management_system.model.api.TaskDTO;
import com.github.bondarevv23.task_management_system.model.api.TaskRequest;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
    @Operation(
            summary = "return all tasks",
            description = "The method returns all tasks. The method supports pagination and filtering.",
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            description = "pagination page number",
                            example = "0"
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            description = "pagination page size",
                            example = "15"
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            description = "pagination page sort",
                            example = "title,desc"
                    ),
                    @Parameter(
                            name = "filter",
                            in = ParameterIn.QUERY,
                            description = "filter for request",
                            example = "id < 100"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "page of all tasks"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user",
                    content = @Content
            )})
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Page<TaskDTO>> findAll(
            @Filter
            @Parameter(hidden = true)
            Specification<Task> specs,
            @Parameter(hidden = true)
            Pageable pageable
    );

    @Operation(
            summary = "return all tasks where user is performer",
            description = "The method returns all tasks that need to be performed by the authenticated user. The method supports pagination.",
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            description = "pagination page number",
                            example = "0"
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            description = "pagination page size",
                            example = "15"
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            description = "pagination page sort",
                            example = "title,desc"
                    ),
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "list of tasks performed by user"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user",
                    content = @Content
            )
    })
    @GetMapping(
            value = "/my",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Page<TaskDTO>> findAllByPerformer(
            @Parameter(hidden = true)
            Pageable pageable
    );

    @Operation(
            summary = "return all tasks owned by user",
            description = "The method returns all tasks that were created by the authenticated user. The method supports pagination.",
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            description = "pagination page number",
                            example = "0"
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            description = "pagination page size",
                            example = "15"
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            description = "pagination page sort",
                            example = "title,desc"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "list of tasks owned by user"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user",
                    content = @Content
            )
    })
    @GetMapping(
            value = "/my/created",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Parameters({
            @Parameter(
                    name = "page",
                    in = ParameterIn.QUERY,
                    description = "pagination page number",
                    example = "0"
            ),
            @Parameter(
                    name = "size",
                    in = ParameterIn.QUERY,
                    description = "pagination page size",
                    example = "15"
            ),
            @Parameter(
                    name = "sort",
                    in = ParameterIn.QUERY,
                    description = "pagination page sort",
                    example = "title,desc"
            ),
            @Parameter(
                    name = "filter",
                    in = ParameterIn.QUERY,
                    description = "filter for request",
                    example = "id < 100"
            )
    })
    ResponseEntity<Page<TaskDTO>> findAllByAuthor(
            @Parameter(hidden = true)
            Pageable pageable
    );

    @Operation(
            summary = "return task by ID",
            description = "The method returns the task by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "corresponded task"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "unauthorized user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "task with this ID doesnt exist",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            )
    })
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<TaskDTO> findById(
            @PathVariable("id")
            @Parameter(
                    in = ParameterIn.PATH,
                    required = true,
                    description = "task ID to fetch",
                    example = "123456789"
            )
            Integer id
    );

    @Operation(
            summary = "create new task",
            description = "The method creates a new task, the author will be an authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "taskDTO of new created task"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "invalid request passed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "performer with passed ID doesnt exist",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            )
    })
    @PostMapping(
            value = "/my",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<TaskDTO> create(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "create request for task",
                    required = true
            )
            TaskRequest request
    );

    @Operation(
            summary = "update task by ID",
            description = "The method updates the task with the corresponding ID by passed request. Only the author of a task can update a task."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "task successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "invalid request passed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "user has no task with this ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            )
    })
    @PutMapping(
            value = "/my/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> update(
            @PathVariable("id")
            @Parameter(
                    in = ParameterIn.PATH,
                    required = true,
                    description = "task ID to update",
                    example = "123456789"
            )
            Integer id,
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "update request for task",
                    required = true
            )
            TaskRequest request
    );

    @Operation(
            summary = "delete task by ID",
            description = "The method deletes a task by its ID. Only the author of a task can delete a task."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "task successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "user has no task with this ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            )
    })
    @DeleteMapping(
            value = "/my/{id}"
    )
    ResponseEntity<Void> delete(
            @PathVariable("id")
            @Parameter(
                    in = ParameterIn.PATH,
                    required = true,
                    description = "task ID to delete",
                    example = "123456789"
            )
            Integer id
    );

    @Operation(
            summary = "change the task status",
            description = "The method changes the status of the task; only the performer of the task can change its status."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "status successfully changed"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "unknown task status passed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "performer has no task with this ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )
            )
    })
    @PostMapping(
            value = "/change-status/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> changeStatus(
            @PathVariable("id")
            @Parameter(
                    in = ParameterIn.PATH,
                    required = true,
                    description = "task ID to update",
                    example = "123456789"
            )
            Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "new status for task",
                    required = true
            )
            @RequestBody Status status
    );
}
