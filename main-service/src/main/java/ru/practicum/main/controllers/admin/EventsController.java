package ru.practicum.main.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.EventService;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.UpdateEventAdminRequest;
import ru.practicum.main.states.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class EventsController {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto>
    getEvents(@RequestParam(required = false) List<Long> users,
              @RequestParam(required = false) List<State> states,
              @RequestParam(required = false) List<Long> categories,
              @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
              @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
              @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest request) {
        return eventService.updateEvent(eventId, request);
    }
}
