package com.christmas.letter.sender.exception;

import com.christmas.letter.sender.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import software.amazon.awssdk.services.sns.model.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @Value("${AWS_SNS_TOPIC_ARN}")
    private String topicArn;

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleAWSSNSNotFoundError(Exception exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                "aws.sns.topic.does.not.exist",
                List.of(Objects.requireNonNullElse(exception.getCause(), "") + " FOR TOPIC " + topicArn));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(Exception exception) {
        MethodArgumentNotValidException notValidException = (MethodArgumentNotValidException) exception;
        List<ObjectError> errors = notValidException.getBindingResult().getAllErrors();

        List<String> reasons = new ArrayList<>();
        for (ObjectError error: errors) {
            reasons.add(error.getDefaultMessage());
        }

        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "failed.request.body.validation", reasons);
    }
}
