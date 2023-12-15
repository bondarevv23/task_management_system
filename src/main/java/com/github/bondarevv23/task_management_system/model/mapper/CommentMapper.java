package com.github.bondarevv23.task_management_system.model.mapper;

import com.github.bondarevv23.task_management_system.model.Comment;
import com.github.bondarevv23.task_management_system.model.api.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "taskId", expression = "java(comment.getTask().getId())")
    CommentDTO commentToCommentDTO(Comment comment);
}
