package ru.practicum.main.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.EventService;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventsControllerPrivate {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEventsByUserId(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("GET - with userId {}", userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEventByUserId(@PathVariable Long userId,
                                            @Valid @RequestBody NewEventDto newEventDto) {
        log.info("POST - Creating event with userId {}", userId);
        return eventService.createEventByUserId(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByUserIdAndEventId(@PathVariable Long userId,
                                                   @PathVariable Long eventId) {
        log.info("GET - event by userId {} and eventId {}", userId, eventId);
        return eventService.getEventByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventWithUserIdAndEventId(@PathVariable Long userId,
                                                        @PathVariable Long eventId,
                                                        @Valid @RequestBody UpdateEventRequest request) {
        log.info("PATCH - event by userId {} and eventId {} and body {}", userId, eventId, request);
        return eventService.updateEventWithUserIdAndEventId(userId, eventId, request);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventByParticipationByUser(@PathVariable Long userId,
                                                                       @PathVariable Long eventId) {
        log.info("Getting event by userId {} participation eventId {}", userId, eventId);
        return eventService.getEventByParticipationByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventResponseStatusUpdateResult
    updateEventParticipationUsers(@PathVariable Long userId,
                                  @PathVariable Long eventId,
                                  @Valid @RequestBody EventRequestStatusUpdateResult request) {
        log.info("Updating eventId {} participation by userId {}", eventId, userId);
        return eventService.updateEventParticipationUsers(userId, eventId, request);
    }
}
