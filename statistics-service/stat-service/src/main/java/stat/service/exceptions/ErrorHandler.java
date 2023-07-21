package stat.service.exceptions;

import lombok.Generated;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dto.dtos.utils.Constants;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;


@RestControllerAdvice
@Generated
public class ErrorHandler {
    private static final String ERROR_REASON_BAD_REQUEST = "Incorrectly made request.";
    private static final String ERROR_REASON_CONFLICT = "Integrity constraint has been violated.";

    @ExceptionHandler({ConstraintViolationException.class, InvalidValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDataException(final RuntimeException e) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                ERROR_REASON_BAD_REQUEST,
                e.getMessage(),
                LocalDateTime.now().format(Constants.formatter));
    }

    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleFilmAlreadyExistException(final RuntimeException e) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                ERROR_REASON_CONFLICT,
                e.getMessage(),
                LocalDateTime.now().format(Constants.formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodValidException(final MethodArgumentNotValidException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason(ERROR_REASON_BAD_REQUEST)
                .message("Error validation Data")
                .timestamp(LocalDateTime.now().format(Constants.formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodValidException(final MissingServletRequestParameterException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason(ERROR_REASON_BAD_REQUEST)
                .message("Error validation Data")
                .timestamp(LocalDateTime.now().format(Constants.formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        return ErrorResponse.builder()
                .message("An unexpected error has occurred.")
                .build();
    }
}