package ru.practicum.main.controllers.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.event.EventService;
import ru.practicum.main.event.dto.EventFullDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.states.EventSortType;
import ru.practicum.main.utils.CommonVariables;

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
public class EventsController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto>
    getEventsWithSortByText(@RequestParam(required = false) String text,
                            @RequestParam(required = false) List<Long> categories,
                            @RequestParam(required = false) Boolean paid,
                            @RequestParam(required = false) @DateTimeFormat(pattern = CommonVariables.DATE_FORMAT) LocalDateTime rangeStart,
                            @RequestParam(required = false) @DateTimeFormat(pattern = CommonVariables.DATE_FORMAT) LocalDateTime rangeEnd,
                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                            @RequestParam(required = false) EventSortType sort,
                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                            @RequestParam(defaultValue = "10") @Positive Integer size,
                            HttpServletRequest request) {
        return eventService.getEventsWithSortByText(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        return eventService.getEventById(id, request);
    }
}
