package ru.practicum.main.comment.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.CommentService;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.CommentDtoRequest;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@Slf4j
public class CommentControllerPrivate {
    private final CommentService service;

    @PostMapping("/{eventId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId,
                                    @Valid @RequestBody CommentDtoRequest commentDtoRequest,
                                    @PathVariable Long eventId) {
        log.info("Creating comment by user {}, for event {}", userId, eventId);
        return service.createComment(eventId, userId, commentDtoRequest);
    }

    @PatchMapping("/comment/{commentId}")
    public CommentDto updateCommentByUser(@PathVariable Long userId,
                                          @Valid @RequestBody CommentDtoRequest commentDtoRequest,
                                          @PathVariable Long commentId) {
        log.info("Updating comment by user {}", userId);
        return service.updateCommentByUser(userId, commentDtoRequest, commentId);
    }

    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByUser(@PathVariable Long userId,
                                    @PathVariable Long commentId) {
        log.info("Deleting comment by user {}", userId);
        service.deleteComment(userId, commentId);
    }
}
