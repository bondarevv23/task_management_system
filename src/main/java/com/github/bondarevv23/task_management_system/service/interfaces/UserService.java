package com.github.bondarevv23.task_management_system.service.interfaces;

import com.github.bondarevv23.task_management_system.model.api.RefreshTokenRequest;
import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;

public interface UserService {
    /**
     * Return bearer token of user by credentials
     * @param credentials user credentials
     * @return bearer token
     */
    TokenResponse getToken(UserCredentials credentials);

    /**
     * Return refreshed bearer token
     * @param request request for refreshing
     * @return bearer token
     */
    TokenResponse refreshToken(RefreshTokenRequest request);
}
