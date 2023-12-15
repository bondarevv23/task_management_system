package com.github.bondarevv23.task_management_system.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class BaseTaskManagementSystemException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
}
