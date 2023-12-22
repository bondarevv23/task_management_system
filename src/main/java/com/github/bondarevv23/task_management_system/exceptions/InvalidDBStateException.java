package com.github.bondarevv23.task_management_system.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidDBStateException extends BaseTaskManagementSystemException {
    public InvalidDBStateException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
