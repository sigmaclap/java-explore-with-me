package ru.practicum.main.request;

import org.springframework.stereotype.Service;
import ru.practicum.main.request.dto.ParticipationRequestDto;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    @Override
    public List<ParticipationRequestDto> getParticipationInEventsByUserId(Long userId) {
        return null;
    }

    @Override
    public ParticipationRequestDto createRequestByUserOnParticipation(Long userId, Long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        return null;
    }
}
