package ru.practicum.main.event;

import org.springframework.stereotype.Service;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.states.EventSortType;
import ru.practicum.main.states.State;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(Integer eventId, UpdateEventAdminRequest request) {
        return null;
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventShortDto createEventByUserId(Long userId, NewEventDto newEventDto) {
        return null;
    }

    @Override
    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDto updateEventWithUserIdAndEventId(Long userId, Long eventId, UpdateEventUserRequest request) {
        return null;
    }

    @Override
    public List<ParticipationRequestDto> getEventByParticipationByUser(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult updateEventParticipationUsers(Long userId, Long eventId, EventRequestStatusUpdateResult request) {
        return null;
    }

    @Override
    public List<EventShortDto> getEventsWithSortByText(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortType sort, Integer from, Integer size, HttpServletRequest request) {
        return null;
    }

    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        return null;
    }
}
