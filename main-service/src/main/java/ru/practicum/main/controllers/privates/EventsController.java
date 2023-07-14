package ru.practicum.main.controllers.privates;

import lombok.RequiredArgsConstructor;
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
public class EventsController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEventsByUserId(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.getEventsByUserId(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventShortDto createEventByUserId(@PathVariable Long userId,
                                             @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.createEventByUserId(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByUserIdAndEventId(@PathVariable Long userId,
                                                   @PathVariable Long eventId) {
        return eventService.getEventByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventWithUserIdAndEventId(@PathVariable Long userId,
                                                        @PathVariable Long eventId,
                                                        @Valid @RequestBody UpdateEventUserRequest request) {
        return eventService.updateEventWithUserIdAndEventId(userId, eventId, request);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventByParticipationByUser(@PathVariable Long userId,
                                                                       @PathVariable Long eventId) {
        return eventService.getEventByParticipationByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult
    updateEventParticipationUsers(@PathVariable Long userId,
                                  @PathVariable Long eventId,
                                  @Valid @RequestBody EventRequestStatusUpdateResult request) {
        return eventService.updateEventParticipationUsers(userId, eventId, request);
    }
}
