package com.christmas.letter.exception;

import com.christmas.letter.model.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    public static final String LETTER_NOT_FOUND_CODE = "letter.not.found";
    public static final String LETTER_NOT_FOUND_MESSAGE = "Could not find letter for email %s ";

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {

        return new ErrorResponse(HttpStatus.EXPECTATION_FAILED.value(), "invalid.sqs.message", List.of(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), LETTER_NOT_FOUND_CODE, List.of(exception.getMessage()));
    }

    @ExceptionHandler(SqsMessageDeserializationException.class)
    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    public ErrorResponse handleInvalidSqsLetterException(SqsMessageDeserializationException exception) {
        return new ErrorResponse(HttpStatus.EXPECTATION_FAILED.value(), "invalid.sqs.message", List.of(exception.getMessage()));
    }
}
