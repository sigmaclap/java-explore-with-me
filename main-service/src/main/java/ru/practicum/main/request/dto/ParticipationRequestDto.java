package ru.practicum.main.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.states.RequestStatus;
import ru.practicum.main.utils.CommonPatterns;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonPatterns.DATE_FORMAT)
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
