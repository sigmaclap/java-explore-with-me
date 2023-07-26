package stat.service.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.dto.dtos.utils.Constants;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Generated
@Builder
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private LocalDateTime timestamp;
}
