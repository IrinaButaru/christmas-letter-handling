package com.christmas.letter.exception;

import com.christmas.letter.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ControllerAdvice
@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    public static final String LETTER_NOT_FOUND_CODE = "letter.not.found";
    public static final String LETTER_NOT_FOUND_MESSAGE = "Could not find letter for email %s ";
    public static final String UNAUTHORIZED_CODE = "not.authorized";
    public static final String ACCESS_DENIED_CODE = "access.denied";
    public static final String ACCESS_DENIED_MESSAGE = "User does not have proper authority to access the requested resource";
    public static final String UNAUTHORIZED_MESSAGE = "Request is unauthorized";
    public static final String INTERNAL_EXCEPTION_CODE = "internal.server.error";

    @ExceptionHandler({ConstraintViolationException.class, SqsMessageDeserializationException.class})
    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {

        return new ErrorResponse(HttpStatus.EXPECTATION_FAILED.value(), "invalid.sqs.message", List.of(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), LETTER_NOT_FOUND_CODE, List.of(exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException exception) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), UNAUTHORIZED_CODE, List.of(exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException exception) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), ACCESS_DENIED_CODE, List.of(ACCESS_DENIED_MESSAGE));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalException(Exception exception) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_EXCEPTION_CODE, List.of(exception.getMessage()));
    }
}
