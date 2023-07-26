package ru.practicum.main.request;

import ru.practicum.main.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getParticipationInEventsByUserId(Long userId);

    ParticipationRequestDto createRequestByUserOnParticipation(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId);
}
