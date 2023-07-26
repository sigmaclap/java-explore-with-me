package ru.practicum.main.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDescriptionsDto {
    String annotation;
    String description;
    String title;
}
