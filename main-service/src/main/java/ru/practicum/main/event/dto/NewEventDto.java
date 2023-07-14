package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.utils.CommonVariables;
import ru.practicum.main.utils.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull
    Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonVariables.DATE_FORMAT)
    @NotNull
    LocalDateTime eventDate;
    @NotNull
    Location location;
    Boolean paid = false;
    @PositiveOrZero
    Long participantLimit = 0L;
    Boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
}
