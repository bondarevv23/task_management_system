package com.github.bondarevv23.task_management_system.exceptions;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends BaseTaskManagementSystemException {
    public AuthorizationException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
