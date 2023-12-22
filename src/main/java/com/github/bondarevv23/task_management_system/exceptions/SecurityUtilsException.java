package com.github.bondarevv23.task_management_system.exceptions;

import org.springframework.http.HttpStatus;

public class SecurityUtilsException extends BaseTaskManagementSystemException {
    public SecurityUtilsException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
