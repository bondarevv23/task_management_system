package com.github.bondarevv23.task_management_system.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    private static final String BAD_REQUEST_MESSAGE = "invalid request passed";

    @ExceptionHandler({ MethodArgumentNotValidException.class, DataAccessException.class })
    public ResponseEntity<Object> handleBadRequest() {
        return ResponseEntity.badRequest().body(new ExceptionResponse(BAD_REQUEST_MESSAGE));
    }

    @ExceptionHandler(BaseTaskManagementSystemException.class)
    public ResponseEntity<Object> handleBaseException(BaseTaskManagementSystemException exception) {
        return ResponseEntity.status(exception.getStatus()).body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleServerException() {
        return ResponseEntity.internalServerError().build();
    }
}
