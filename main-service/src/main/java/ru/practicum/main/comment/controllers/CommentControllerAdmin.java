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
@RequestMapping("/admin/events")
@Slf4j
public class CommentControllerAdmin {
    private final CommentService service;

    @PatchMapping("/comment/{commentId}")
    public CommentDto updateCommentByAdmin(@PathVariable Long commentId,
                                           @Valid @RequestBody CommentDtoRequest commentDto) {
        log.info("Updating comment by admin");
        return service.updateCommentByAdmin(commentId, commentDto);
    }

    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        log.info("Deleting comment by admin");
        service.deleteCommentByAdmin(commentId);
    }
}
