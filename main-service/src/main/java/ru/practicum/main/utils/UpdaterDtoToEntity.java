package ru.practicum.main.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.main.category.CategoryRepository;
import ru.practicum.main.event.dto.UpdateEventRequest;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.exceptions.InvalidDataException;
import ru.practicum.main.states.State;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdaterDtoToEntity {
    private final CategoryRepository categoryRepository;

    public void fromDtoToEntity(UpdateEventRequest dto, Event event) {
        if (dto.getAnnotation() != null && !dto.getAnnotation().isBlank()) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getCategory() != null) {
            event.setCategory(categoryRepository.getReferenceById(dto.getCategory()));
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLocation(dto.getLocation());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getStateAction() != null) {
            updateStatusState(dto, event);
        }
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            event.setTitle(dto.getTitle());
        }
    }

    private void updateStatusState(UpdateEventRequest dto, Event event) {
        switch (dto.getStateAction()) {
            case PUBLISH_EVENT:
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case SEND_TO_REVIEW:
                event.setState(State.PENDING);
                break;
            case REJECT_EVENT:
                if (event.getState().equals(State.PUBLISHED)) {
                    log.error("The event has been published and cannot be rejected.");
                    throw new InvalidDataException("The event has been published and cannot be rejected.");
                }
                event.setState(State.CANCELED);
                break;
            case CANCEL_REVIEW:
                event.setState(State.CANCELED);
                break;
        }
    }
}
