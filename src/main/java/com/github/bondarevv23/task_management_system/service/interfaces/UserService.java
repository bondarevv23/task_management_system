package com.github.bondarevv23.task_management_system.service.interfaces;

import com.github.bondarevv23.task_management_system.model.api.RefreshTokenRequest;
import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;

public interface UserService {
    TokenResponse getToken(UserCredentials credentials);

    TokenResponse refreshToken(RefreshTokenRequest request);
}
