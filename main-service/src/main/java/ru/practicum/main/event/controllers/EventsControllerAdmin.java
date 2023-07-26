package ru.practicum.main.event.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.EventService;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.UpdateEventRequest;
import ru.practicum.main.states.State;
import ru.practicum.main.utils.CommonPatterns;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class EventsControllerAdmin {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto>
    getEvents(@RequestParam(required = false) List<Long> users,
              @RequestParam(required = false) List<State> states,
              @RequestParam(required = false) List<Long> categories,
              @RequestParam(required = false) @DateTimeFormat(pattern = CommonPatterns.DATE_FORMAT) LocalDateTime rangeStart,
              @RequestParam(required = false) @DateTimeFormat(pattern = CommonPatterns.DATE_FORMAT) LocalDateTime rangeEnd,
              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
              @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEventByAdmin(@Valid @RequestBody UpdateEventRequest request,
                                          @PathVariable Long eventId) {
        return eventService.patchEventByAdmin(eventId, request);
    }
}
