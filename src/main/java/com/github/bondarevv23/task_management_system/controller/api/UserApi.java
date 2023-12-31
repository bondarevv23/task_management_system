package com.github.bondarevv23.task_management_system.controller.api;

import com.github.bondarevv23.task_management_system.model.api.ExceptionResponse;
import com.github.bondarevv23.task_management_system.model.api.RefreshTokenRequest;
import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/users")
@Tag(name = "users")
public interface UserApi {

    @Operation(
            summary = "log in to user account",
            description = "The method accepts the user credentials and tries to authenticate him."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "user successfully logged in"
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
                    description = "invalid user credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    ))})
    @PostMapping(
            path = "/get-token",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<TokenResponse> getToken(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "user credentials",
                    required = true
            )
            UserCredentials credentials);

    @Operation(
            summary = "refresh jwt",
            description = "The method accepts the refresh token and tries to refresh bearer token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "token successfully refreshed"
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
                    description = "invalid user credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class)
                    ))})
    @PostMapping(
            path = "/refresh-token",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<TokenResponse> refreshToken(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "jwt token to refresh",
                    required = true
            )
            RefreshTokenRequest request);

}
