package com.github.bondarevv23.task_management_system.exceptions;

import org.springframework.http.HttpStatus;

public class KeycloakErrorException extends BaseTaskManagementSystemException {
    public KeycloakErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
