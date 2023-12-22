package com.github.bondarevv23.task_management_system.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseTaskManagementSystemException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
