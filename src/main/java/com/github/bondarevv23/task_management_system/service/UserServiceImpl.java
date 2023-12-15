package com.github.bondarevv23.task_management_system.service;

import com.github.bondarevv23.task_management_system.client.interfaces.KeycloakClient;
import com.github.bondarevv23.task_management_system.model.api.RefreshTokenRequest;
import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;
import com.github.bondarevv23.task_management_system.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final KeycloakClient keycloakClient;

    @Override
    public TokenResponse getToken(UserCredentials credentials) {
        return keycloakClient.getToken(credentials);
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        return keycloakClient.refreshToken(request);
    }
}
