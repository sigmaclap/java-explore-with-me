package ru.practicum.main.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.user.entity.User;
import ru.practicum.main.utils.CommonPatterns;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoRequest {
    private Long id;
    @NotNull
    private String text;
    private Event event;
    private User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonPatterns.DATE_FORMAT)
    private final LocalDateTime createdDate = LocalDateTime.now();
}
