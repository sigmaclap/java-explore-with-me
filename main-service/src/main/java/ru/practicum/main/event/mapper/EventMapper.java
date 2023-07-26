package ru.practicum.main.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.category.mapper.CategoryMapper;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.states.RequestStatus;
import ru.practicum.main.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

    public EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDtoCategory(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .category(event.getCategory() != null ? CategoryMapper.toDtoCategory(event.getCategory()) : null)
                .confirmedRequests(event.getConfirmedRequests())
                .eventDescriptions(EventDescriptionsDto.builder()
                        .annotation(event.getAnnotation())
                        .description(event.getDescription())
                        .title(event.getTitle())
                        .build())
                .eventDates(EventDateTimeDto.builder()
                        .createdOn(event.getCreatedOn())
                        .eventDate(event.getEventDate())
                        .publishedOn(event.getPublishedOn())
                        .build())
                .initiator(event.getInitiator() != null ? UserMapper.toShortDto(event.getInitiator()) : null)
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .views(event.getViews())
                .build();
    }

    public Event toEvent(NewEventDto dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .location(dto.getLocation())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .build();
    }

    public EventResponseStatusUpdateResult toDtoResponseStatus(List<ParticipationRequestDto> requestsDto) {
        return EventResponseStatusUpdateResult.builder()
                .confirmedRequests(requestsDto.stream()
                        .filter(stat -> stat.getStatus().equals(RequestStatus.CONFIRMED))
                        .collect(Collectors.toList()))
                .rejectedRequests(requestsDto.stream()
                        .filter(stat -> stat.getStatus().equals(RequestStatus.REJECTED))
                        .collect(Collectors.toList()))
                .build();
    }
}
