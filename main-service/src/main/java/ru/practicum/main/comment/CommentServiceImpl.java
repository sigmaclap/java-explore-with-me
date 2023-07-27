package ru.practicum.main.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.CommentDtoRequest;
import ru.practicum.main.comment.entity.Comment;
import ru.practicum.main.comment.mapper.CommentMapper;
import ru.practicum.main.event.EventRepository;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.exceptions.*;
import ru.practicum.main.states.State;
import ru.practicum.main.user.UserRepository;
import ru.practicum.main.user.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto createComment(Long eventId, Long userId, CommentDtoRequest dto) {
        Comment comment = CommentMapper.toComment(dto);
        User user = validateExistsUser(userId);
        Event event = validateExistsEvent(eventId);
        isCheckUserNotInitiator(user, event);
        isCheckEventPublished(event.getId());
        comment.setEvent(event);
        comment.setUser(user);
        return CommentMapper.toCommentDto(repository.save(comment));
    }

    @Override
    public CommentDto updateCommentByUser(Long userId, CommentDtoRequest dto, Long commentId) {
        User user = validateExistsUser(userId);
        Comment comment = validateExistsComment(commentId);
        isCommentUserOwner(user, comment);
        isCheckTimeToChange(comment);
        comment.setText(dto.getText());
        return CommentMapper.toCommentDto(repository.save(comment));
    }


    @Override
    public CommentDto updateCommentByAdmin(Long commentId, CommentDtoRequest dto) {
        Comment comment = validateExistsComment(commentId);
        comment.setText(dto.getText());
        return CommentMapper.toCommentDto(repository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        User user = validateExistsUser(userId);
        Comment comment = validateExistsComment(commentId);
        isCommentUserOwner(user, comment);
        repository.delete(comment);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        Comment comment = validateExistsComment(commentId);
        repository.delete(comment);
    }

    private static void isCommentUserOwner(User user, Comment comment) {
        if (!user.equals(comment.getUser())) {
            throw new BadRequestException("Only a moderator or user who left a comment can change it.");
        }
    }

    private void isCheckEventPublished(Long eventId) {
        Optional<Event> events = eventRepository.findByIdAndState(eventId, State.PUBLISHED);
        if (events.isEmpty()) {
            throw new BadRequestException("You cannot leave a comment on an event that has not yet been published.");
        }
    }

    private User validateExistsUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id not found: " + userId));
    }

    private Event validateExistsEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id was not found: " + eventId));
    }

    private Comment validateExistsComment(Long commentId) {
        return repository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id was not found: " + commentId));
    }

    private static void isCheckUserNotInitiator(User user, Event event) {
        if (user.equals(event.getInitiator())) {
            throw new BadRequestException("An event initiator cannot post a comment on their own event.");
        }
    }

    private static void isCheckTimeToChange(Comment comment) {
        LocalDateTime createdTime = comment.getCreatedDate();
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(createdTime.plusHours(1))) {
            throw new InvalidDataException("You can only edit a comment within an hour.");
        }
    }
}
