package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.states.State;
import ru.practicum.main.user.dto.UserShortDto;
import ru.practicum.main.utils.Location;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;
    CategoryDto category;
    Long confirmedRequests;
    @JsonUnwrapped
    EventDescriptionsDto eventDescriptions;
    @JsonUnwrapped
    EventDateTimeDto eventDates;
    UserShortDto initiator;
    Location location;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    State state;
    Long views;
}
