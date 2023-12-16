package com.github.bondarevv23.task_management_system.service.interfaces;

import com.github.bondarevv23.task_management_system.model.api.CommentDTO;
import com.github.bondarevv23.task_management_system.model.api.CommentRequest;

import java.util.UUID;

public interface CommentService {
    /**
     * Write new comment with author and comment request
     * @param authorId id of author
     * @param request request of new comment
     * @return commentDTO of new comment
     */
    CommentDTO write(UUID authorId, CommentRequest request);

    /**
     * Delete comment with passed ID. Only author of comment can delete it.
     * @param authorId id of author
     * @param id id of comment for removing
     */
    void delete(UUID authorId, Integer id);

    /**
     * Update comment with corresponded ID by passed request. Only author can update comment.
     * @param authorId id of author
     * @param id id of comment for updating
     * @param request update request
     */
    void update(UUID authorId, Integer id, CommentRequest request);

    /**
     * Return comment with corresponding ID.
     * @param id id of comment
     * @return commentDTO of comment
     */
    CommentDTO get(Integer id);
}
