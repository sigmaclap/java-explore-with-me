package ru.practicum.main.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.EventService;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.exceptions.BadRequestException;
import ru.practicum.main.states.EventSortType;
import ru.practicum.main.utils.CommonPatterns;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventsControllerPublic {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto>
    getEventsWithSortByText(@RequestParam(required = false) String text,
                            @RequestParam(required = false) List<Long> categories,
                            @RequestParam(required = false) Boolean paid,
                            @RequestParam(required = false) @DateTimeFormat(pattern = CommonPatterns.DATE_FORMAT) LocalDateTime rangeStart,
                            @RequestParam(required = false) @DateTimeFormat(pattern = CommonPatterns.DATE_FORMAT) LocalDateTime rangeEnd,
                            @RequestParam(defaultValue = "false") boolean onlyAvailable,
                            @RequestParam(required = false) EventSortType sort,
                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                            @RequestParam(defaultValue = "10") @Positive Integer size,
                            HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Start time must be before end time!");
        }
        return eventService.getEventsWithSortByText(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("get event by ID {}", id);
        return eventService.getEventById(id, request);
    }
}
