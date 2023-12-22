package com.github.bondarevv23.task_management_system.controller;

import com.github.bondarevv23.task_management_system.model.api.RefreshTokenRequest;
import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;
import com.github.bondarevv23.task_management_system.util.IntegrationEnvironment;
import com.github.bondarevv23.task_management_system.util.WrongData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static com.github.bondarevv23.task_management_system.generator.RequestGenerator.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest extends IntegrationEnvironment {

    @Test
    void whenGetTokenWithRightCredentials_thenTokenReturns() {
        // given
        UserCredentials rightCredentials = getRightCredentials();

        // when
        ResponseEntity<TokenResponse> response = client.post()
                .uri("/api/v1/users/get-token")
                .bodyValue(rightCredentials)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TokenResponse.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasNoNullFieldsOrProperties();
    }

    @Test
    void whenGetTokenWithWrongCredentials_thenUnauthorized() {
        // given
        UserCredentials wrongCredentials = getWrongCredentials();

        // when
        ResponseEntity<TokenResponse> response = client.post()
                .uri("/api/v1/users/get-token")
                .bodyValue(wrongCredentials)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TokenResponse.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenGetTokenWithWrongData_thenBadRequest() {
        // given
        WrongData wrongData = getWrongData();

        // when
        ResponseEntity<TokenResponse> response = client.post()
                .uri("/api/v1/users/get-token")
                .bodyValue(wrongData)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TokenResponse.class)
                .block();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenRefreshTokenByRightRequest_thenNeTokenResponseReturns() {
        // given
        String refreshToken = client.post()
                .uri("/api/v1/users/get-token")
                .bodyValue(getRightCredentials())
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TokenResponse.class)
                .block().getBody().getRefreshToken();
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        // when
        ResponseEntity<TokenResponse> refreshed = client.post()
                .uri("/api/v1/users/refresh-token")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TokenResponse.class)
                .block();

        // then
        assertThat(refreshed).isNotNull();
        assertThat(refreshed.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(refreshed.getBody()).hasNoNullFieldsOrProperties();
    }

    @Test
    void whenRefreshTokenWithInvalidCode_thenUnauthorized() {
        // given
        String invalidRefreshCode = "some code";
        RefreshTokenRequest request = new RefreshTokenRequest(invalidRefreshCode);

        // when
        ResponseEntity<TokenResponse> refreshed = client.post()
                .uri("/api/v1/users/refresh-token")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TokenResponse.class)
                .block();

        // then
        assertThat(refreshed).isNotNull();
        assertThat(refreshed.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenRefreshTokenWithWrongData_thenBadRequest() {
        // given
        WrongData wrongData = getWrongData();

        // when
        ResponseEntity<TokenResponse> refreshed = client.post()
                .uri("/api/v1/users/refresh-token")
                .bodyValue(wrongData)
                .retrieve()
                .onStatus(HttpStatusCode::isError, res -> Mono.empty())
                .toEntity(TokenResponse.class)
                .block();

        // then
        assertThat(refreshed).isNotNull();
        assertThat(refreshed.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
