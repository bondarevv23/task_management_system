package com.github.bondarevv23.task_management_system.client;


import com.github.bondarevv23.task_management_system.client.interfaces.KeycloakClient;
import com.github.bondarevv23.task_management_system.exceptions.KeycloakAuthorizationException;
import com.github.bondarevv23.task_management_system.exceptions.KeycloakErrorException;
import com.github.bondarevv23.task_management_system.model.api.RefreshTokenRequest;
import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.github.bondarevv23.task_management_system.client.constants.KeycloakConstants.*;

@Component
@RequiredArgsConstructor
public class KeycloakClientImpl implements KeycloakClient {
    private final WebClient keycloakWebClient;

    public TokenResponse getToken(UserCredentials credentials) {
        return keycloakWebClient.post().body(
                        BodyInserters.fromFormData(CLIENT_ID, TASKS_CLIENT)
                                .with(GRANT_TYPE, PASSWORD)
                                .with(USERNAME, credentials.getEmail())
                                .with(PASSWORD, credentials.getPassword())
                )
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleUnauthorizedResponse)
                .onStatus(HttpStatusCode::isError, this::handleUnexpectedResponse)
                .bodyToMono(TokenResponse.class)
                .block();
    }

    public TokenResponse refreshToken(RefreshTokenRequest refreshToken) {
        return keycloakWebClient.post().body(
                        BodyInserters.fromFormData(CLIENT_ID, TASKS_CLIENT)
                                .with(GRANT_TYPE, REFRESH_TOKEN)
                                .with(REFRESH_TOKEN, refreshToken.getRefreshToken())
                )
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleUnauthorizedResponse)
                .onStatus(HttpStatusCode::isError, this::handleUnexpectedResponse)
                .bodyToMono(TokenResponse.class)
                .block();
    }

    private Mono<Exception> handleUnauthorizedResponse(ClientResponse response) {
        throw new KeycloakAuthorizationException("invalid user credentials");
    }

    private Mono<Exception> handleUnexpectedResponse(ClientResponse response) {
        throw new KeycloakErrorException("unexpected keycloak server response");
    }
}
