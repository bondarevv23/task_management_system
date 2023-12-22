package com.github.bondarevv23.task_management_system.client.interfaces;

import com.github.bondarevv23.task_management_system.model.api.RefreshTokenRequest;
import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;

public interface KeycloakClient {
    TokenResponse getToken(UserCredentials credentials);

    TokenResponse refreshToken(RefreshTokenRequest refreshToken);
}
