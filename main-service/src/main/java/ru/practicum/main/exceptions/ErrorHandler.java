package ru.practicum.main.exceptions;

import lombok.Generated;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestControllerAdvice
@Generated
public class ErrorHandler {
    private static final String ERROR_REASON_BAD_REQUEST = "Incorrectly made request.";
    private static final String ERROR_REASON_CONFLICT = "Integrity constraint has been violated.";
    private static final String ERROR_REASON_NOT_FOUND = "The required object was not found.";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler({ConstraintViolationException.class, InvalidValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDataException(final RuntimeException e) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                ERROR_REASON_BAD_REQUEST,
                e.getMessage(),
                LocalDateTime.now().format(DATE_FORMAT));
    }

    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleFilmAlreadyExistException(final RuntimeException e) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.name(),
                ERROR_REASON_CONFLICT,
                e.getMessage(),
                LocalDateTime.now().format(DATE_FORMAT));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(final RuntimeException e) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.name(),
                ERROR_REASON_NOT_FOUND,
                e.getMessage(),
                LocalDateTime.now().format(DATE_FORMAT));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodValidException(final MethodArgumentNotValidException e) {
        return ErrorResponse.builder()
                .message("Error validation Data")
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