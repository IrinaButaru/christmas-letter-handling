package com.christmas.letter.sender.exception;

import com.christmas.letter.sender.dto.ErrorDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import software.amazon.awssdk.services.sns.model.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @Value("${AWS_SNS_TOPIC_ARN}")
    private String topicArn;

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorDto handleAWSSNSNotFoundError(Exception exception) {
        return new ErrorDto(HttpStatus.NOT_FOUND.value(),
                "aws.sns.topic.does.not.exist",
                List.of(exception.getCause().toString() + " FOR TOPIC " + topicArn));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto handleBadRequestException(Exception exception) {
        MethodArgumentNotValidException notValidException = (MethodArgumentNotValidException) exception;
        List<ObjectError> errors = notValidException.getBindingResult().getAllErrors();

        List<String> reasons = new ArrayList<>();
        for (ObjectError error: errors) {
            reasons.add(error.getDefaultMessage());
        }

        return new ErrorDto(HttpStatus.BAD_REQUEST.value(), "failed.request.body.validation", reasons);
    }
}
