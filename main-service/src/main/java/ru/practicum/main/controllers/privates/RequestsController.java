package ru.practicum.main.controllers.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.request.RequestService;
import ru.practicum.main.request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/requests")
public class RequestsController {
    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getParticipationInEventsByUserId(@PathVariable Long userId) {
        return requestService.getParticipationInEventsByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequestByUserOnParticipation(@PathVariable Long userId,
                                                                      @PathVariable Long eventId) {
        return requestService.createRequestByUserOnParticipation(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable Long userId,
                                                       @PathVariable Long requestId) {
        return requestService.cancelRequestByUser(userId, requestId);
    }
}
