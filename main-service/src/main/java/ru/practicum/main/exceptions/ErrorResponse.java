package ru.practicum.main.exceptions;

import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Generated
@Builder
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}
