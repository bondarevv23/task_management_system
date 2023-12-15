package com.github.bondarevv23.task_management_system.service;

import com.github.bondarevv23.task_management_system.client.interfaces.KeycloakClient;
import com.github.bondarevv23.task_management_system.exceptions.KeycloakAuthorizationException;
import com.github.bondarevv23.task_management_system.model.api.RefreshTokenRequest;
import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;
import com.github.bondarevv23.task_management_system.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.bondarevv23.task_management_system.generator.RequestGenerator.*;
import static com.github.bondarevv23.task_management_system.generator.ResponseGenerator.getRightTokenResponse;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private KeycloakClient keycloakClient;
    private UserService service;

    @BeforeEach
    void init() {
        keycloakClient = mock(KeycloakClient.class);
        service = new UserServiceImpl(keycloakClient);
        when(keycloakClient.getToken(getRightCredentials())).thenReturn(getRightTokenResponse());
        when(keycloakClient.getToken(getWrongCredentials()))
                .thenThrow(new KeycloakAuthorizationException("invalid credentials"));
        when(keycloakClient.refreshToken(getRightRefreshToken())).thenReturn(getRightTokenResponse());
        when(keycloakClient.refreshToken(getWrongRefreshToken()))
                .thenThrow(new KeycloakAuthorizationException("invalid refresh token"));
    }

    @Test
    void whenGetTokenWithRightCredentials_thenTokenReturns() {
        // given
        UserCredentials rightCredentials = getRightCredentials();

        // when
        TokenResponse token = service.getToken(rightCredentials);

        // then
        assertThat(token).isEqualTo(getRightTokenResponse());
        verify(keycloakClient, times(1)).getToken(getRightCredentials());
    }

    @Test
    void whenGetTokenWithInvalidCredentials_thenKeycloakAuthorizationExceptionThrows() {
        // given
        UserCredentials wrongCredentials = getWrongCredentials();

        // when

        // then
        assertThatThrownBy(() -> service.getToken(wrongCredentials)).isInstanceOf(KeycloakAuthorizationException.class);
        verify(keycloakClient, times(1)).getToken(wrongCredentials);
    }

    @Test
    void whenRefreshTokenWithRightRequest_thenTokenReturns() {
        // given
        RefreshTokenRequest request = getRightRefreshToken();

        // when
        TokenResponse token = service.refreshToken(request);

        // then
        assertThat(token).isEqualTo(getRightTokenResponse());
        verify(keycloakClient, times(1)).refreshToken(request);
    }

    @Test
    void whenRefreshTokenWithInvalidRequest_thenKeycloakAuthorizationException() {
        // given
        RefreshTokenRequest request =getWrongRefreshToken();

        // when

        // then
        assertThatThrownBy(() -> service.refreshToken(request)).isInstanceOf(KeycloakAuthorizationException.class);
        verify(keycloakClient, times(1)).refreshToken(request);
    }
}
