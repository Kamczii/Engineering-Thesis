package org.once_a_day.messenger.messenger_app.handlers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.once_a_day.dto.ApplicationExceptionDTO;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationExceptionDTO> handleApplicationException(ApplicationException ex) {
        final var dto = map(ex);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApplicationExceptionDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        final var dto = map(ex);
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApplicationExceptionDTO> handleApplicationException(ConstraintViolationException ex) {
        final var message = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n"));
        final ApplicationExceptionDTO dto = ApplicationExceptionDTO.builder()
                .code(ExceptionCode.INVALID_INPUT)
                .message(message)
                .build();
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    private ApplicationExceptionDTO map(final ResourceNotFoundException ex) {
        final var message = String.join(" ", ex.getClazz().getSimpleName(), ex.getField(), ex.getValue());
        return ApplicationExceptionDTO.builder()
                .code(ExceptionCode.NOT_FOUND)
                .message(message)
                .build();
    }

    private ApplicationExceptionDTO map(ApplicationException ex) {
        return ApplicationExceptionDTO.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
    }
}
