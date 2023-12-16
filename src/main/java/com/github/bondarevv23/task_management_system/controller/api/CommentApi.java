package com.github.bondarevv23.task_management_system.controller.api;

import com.github.bondarevv23.task_management_system.exceptions.ExceptionResponse;
import com.github.bondarevv23.task_management_system.model.api.CommentDTO;
import com.github.bondarevv23.task_management_system.model.api.CommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RequestMapping("api/v1/comments")
@Tag(name = "comments")
public interface CommentApi {
    @Operation(
            summary = "write new comment",
            description = "The method accepts a request to write a comment and tries to create a new comment, the author will be an authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "comment successfully wrote"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "invalid request passed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "task with this ID doesnt exist",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    ))})
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CommentDTO> write(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "write request for comment",
                    required = true
            )
            CommentRequest request
    );

    @Operation(
            summary = "update an existing comment",
            description = "The method updates the comment with the corresponding ID by passed request. Only the author of a comment can update a comment."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "comment successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "invalid request passed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "authenticated user has no comment with this ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    ))})
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Void> update(
            @PathVariable("id")
            @Parameter(
                    in = ParameterIn.PATH,
                    required = true,
                    description = "comment ID",
                    example = "123456789"
            )
            Integer id,
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "update request for comment",
                    required = true
            )
            CommentRequest request
    );

    @Operation(
            summary = "get comment by ID",
            description = "The method returns the comment by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "comment successfully returned"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "there is no comment with this ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    ))})
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CommentDTO> get(
            @PathVariable("id")
            @Parameter(
                    in = ParameterIn.PATH,
                    required = true,
                    description = "comment ID to fetch",
                    example = "123456789"
            )
            Integer id
    );

    @Operation(
            summary = "delete comment",
            description = "The method deletes a comment by its ID. Only the author of a comment can delete a comment."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "comment successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "failed to authenticate user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "the user has no comments with this ID",
                    content = @Content
            )})
    @DeleteMapping(
            value = "/{id}"
    )
    ResponseEntity<Void> delete(
            @PathVariable("id")
            @Parameter(
                    in = ParameterIn.PATH,
                    required = true,
                    description = "comment ID to remove",
                    example = "123456789"
            )
            Integer id);
}
