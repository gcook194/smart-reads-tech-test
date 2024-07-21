package uk.co.scottishpower.smartreads.web.controlleradvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import uk.co.scottishpower.smartreads.error.NotFoundException;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final Clock clock;

    @ExceptionHandler(value = {
            HttpClientErrorException.class,
            NotFoundException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handle404s(Exception ex, WebRequest request) {
        log.error("Exception thrown: ", ex);
        return ErrorMessage.builder()
                .status(HttpStatus.NOT_FOUND)
                .date(ZonedDateTime.now(clock))
                .message(ex.getMessage())
                .description(request.getDescription(true))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseBody handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException thrown: ", ex);
        final List<Error> errors = ex.getAllErrors()
                .stream()
                .map(e -> {
                    final String fieldName = e instanceof FieldError fieldError ? fieldError.getField() : e.getObjectName();
                    return Error.builder()
                            .fieldName(fieldName)
                            .errorMessage(e.getDefaultMessage())
                            .build();
                })
                .toList();

        return ErrorResponseBody.builder()
                .message("Validation failure")
                .errors(errors)
                .invalidData(ex.getBindingResult().getTarget())
                .build();
    }
}
