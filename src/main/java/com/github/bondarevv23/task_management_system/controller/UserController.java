package com.github.bondarevv23.task_management_system.controller;

import com.github.bondarevv23.task_management_system.controller.api.UserApi;
import com.github.bondarevv23.task_management_system.model.api.RefreshTokenRequest;
import com.github.bondarevv23.task_management_system.model.api.TokenResponse;
import com.github.bondarevv23.task_management_system.model.api.UserCredentials;
import com.github.bondarevv23.task_management_system.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public ResponseEntity<TokenResponse> getToken(UserCredentials credentials) {
        return ResponseEntity.ok(userService.getToken(credentials));
    }

    @Override
    public ResponseEntity<TokenResponse> refreshToken(RefreshTokenRequest request) {
        return ResponseEntity.ok(userService.refreshToken(request));
    }
}
