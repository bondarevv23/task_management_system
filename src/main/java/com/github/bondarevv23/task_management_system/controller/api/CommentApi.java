package com.github.bondarevv23.task_management_system.controller.api;

import com.github.bondarevv23.task_management_system.model.api.CommentDTO;
import com.github.bondarevv23.task_management_system.model.api.CommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/comments")
@Tag(name = "comments")
public interface CommentApi {
    @Operation(summary = "write new comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "comment successfully wrote",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDTO.class)
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
                    description = "task with this id doesnt exist"
            )})
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CommentDTO> write(@Valid @RequestBody CommentRequest request);

    @Operation(summary = "update an existing comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "comment successfully updated"
            ),
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
                    description = "authenticated user has no comment with such id"
            )})
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> update(@PathVariable("id") Integer id, @Valid @RequestBody CommentRequest request);

    @Operation(summary = "get comment by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "comment successfully returned",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDTO.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "there is no comment with this id"
            )})
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CommentDTO> get(@PathVariable("id") Integer id);

    @Operation(summary = "delete comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "comment successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "the user has no comments with this id"
            )})
    @DeleteMapping(
            value = "/{id}"
    )
    ResponseEntity<Void> delete(@PathVariable("id") Integer id);
}
