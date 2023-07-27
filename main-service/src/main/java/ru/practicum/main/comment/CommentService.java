package ru.practicum.main.comment;

import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.CommentDtoRequest;

public interface CommentService {
    CommentDto createComment(Long eventId, Long userId, CommentDtoRequest dto);

    CommentDto updateCommentByUser(Long userId, CommentDtoRequest dto, Long commentId);

    CommentDto updateCommentByAdmin(Long commentId, CommentDtoRequest dto);

    void deleteComment(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);
}
