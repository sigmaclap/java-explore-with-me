package ru.practicum.main.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.entity.Request;

@UtilityClass
public class RequestMapper {
    public ParticipationRequestDto toDtoRequest(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
