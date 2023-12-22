package com.github.bondarevv23.task_management_system;

import com.github.bondarevv23.task_management_system.model.Priority;
import com.github.bondarevv23.task_management_system.model.Status;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants {
    public static final String TASK_TITLE = "test task title ";
    public static final String TASK_DESCRIPTION = "test task description ";
    public static final Status TASK_STATUS = Status.IN_PROGRESS;
    public static final Priority TASK_PRIORITY = Priority.HIGH;
    public static final String COMMENT_TEXT = "test comment text ";
}
