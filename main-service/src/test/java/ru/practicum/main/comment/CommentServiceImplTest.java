package ru.practicum.main.comment;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.CommentDtoRequest;
import ru.practicum.main.comment.entity.Comment;
import ru.practicum.main.comment.mapper.CommentMapper;
import ru.practicum.main.event.EventRepository;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.exceptions.BadRequestException;
import ru.practicum.main.exceptions.InvalidDataException;
import ru.practicum.main.states.State;
import ru.practicum.main.user.UserRepository;
import ru.practicum.main.user.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    CommentServiceImpl service;
    private final EasyRandom generator = new EasyRandom();
    private User user;
    private Event event;
    private CommentDtoRequest dto;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = generator.nextObject(User.class);
        event = generator.nextObject(Event.class);
        event.setState(State.PUBLISHED);
        dto = CommentDtoRequest.builder()
                .event(event)
                .user(user)
                .text("text")
                .build();
        comment = CommentMapper.toComment(dto);
        comment.setUser(dto.getUser());
        comment.setEvent(dto.getEvent());
    }

    @Test
    void createComment_whenValidatedData_thenCreateComment() {
        CommentDto expectedComment = CommentMapper.toCommentDto(comment);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(eventRepository.findByIdAndState(event.getId(), State.PUBLISHED))
                .thenReturn(Optional.of(event));
        when(repository.save(comment)).thenReturn(comment);

        CommentDto actualComment = service.createComment(event.getId(), user.getId(), dto);

        assertEquals(expectedComment, actualComment);
        verify(repository, times(1)).save(comment);
    }

    @Test
    void createComment_whenEventNotPublished_thenReturnedThrows() {
        event.setState(State.PENDING);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(eventRepository.findByIdAndState(event.getId(), State.PUBLISHED))
                .thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> service.createComment(event.getId(), user.getId(), dto));
        verify(repository, never()).save(comment);
    }

    @Test
    void createComment_whenInitiatorEventTryToPostCommentOwnEvent_thenReturnedThrows() {
        User userInitiator = event.getInitiator();
        when(userRepository.findById(userInitiator.getId())).thenReturn(Optional.of(userInitiator));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));


        Throwable ex = assertThrows(BadRequestException.class, () -> service.createComment(event.getId(), userInitiator.getId(), dto));
        assertEquals("An event initiator cannot post a comment on their own event.", ex.getMessage());
        verify(repository, never()).save(comment);
    }

    @Test
    void updateCommentByUser_whenValidData_thenUpdatedComment() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.findById(comment.getId())).thenReturn(Optional.of(comment));
        comment.setText(dto.getText());
        when(repository.save(comment)).thenReturn(comment);
        CommentDto expected = CommentMapper.toCommentDto(comment);

        CommentDto actual = service.updateCommentByUser(user.getId(), dto, comment.getId());

        assertEquals(expected, actual);
        verify(repository, times(1)).save(comment);
    }

    @Test
    void updateCommentByUser_whenUserNotOwner_thenReturnedThrows() {
        User userNotCreated = User.builder()
                .id(1L)
                .name("name")
                .email("email@gmail.com")
                .build();
        when(userRepository.findById(userNotCreated.getId())).thenReturn(Optional.of(userNotCreated));
        when(repository.findById(comment.getId())).thenReturn(Optional.of(comment));

        Throwable ex = assertThrows(BadRequestException.class,
                () -> service.updateCommentByUser(userNotCreated.getId(), dto, comment.getId()));
        assertEquals("Only a moderator or user who left a comment can change it.", ex.getMessage());
        verify(repository, never()).save(comment);
    }

    @Test
    void updateCommentByUser_whenTimeOverToChange_thenReturnedThrows() {
        comment.setCreatedDate(LocalDateTime.now().minusDays(3));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.findById(comment.getId())).thenReturn(Optional.of(comment));

        Throwable ex = assertThrows(InvalidDataException.class,
                () -> service.updateCommentByUser(user.getId(), dto, comment.getId()));
        assertEquals("You can only edit a comment within an hour.", ex.getMessage());
        verify(repository, never()).save(comment);
    }

    @Test
    void updateCommentByAdmin_whenValidData_thenUpdateComment() {
        when(repository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(repository.save(comment)).thenReturn(comment);
        comment.setText(dto.getText());
        CommentDto expected = CommentMapper.toCommentDto(comment);

        CommentDto actual = service.updateCommentByAdmin(comment.getId(), dto);

        assertEquals(expected, actual);
        verify(repository, times(1)).save(comment);
    }

    @Test
    void deleteComment_whenValidData_ThenDeletedComment() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.findById(comment.getId())).thenReturn(Optional.of(comment));

        service.deleteComment(user.getId(), comment.getId());

        verify(repository, times(1)).delete(comment);
    }

    @Test
    void deleteCommentByAdmin_whenValidData_thenDeletedCommentByAdmin() {
        when(repository.findById(comment.getId())).thenReturn(Optional.of(comment));

        service.deleteCommentByAdmin(comment.getId());

        verify(repository, times(1)).delete(comment);
    }
}