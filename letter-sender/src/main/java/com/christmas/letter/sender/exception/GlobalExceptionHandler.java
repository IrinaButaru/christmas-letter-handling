package com.christmas.letter.sender.exception;

import com.christmas.letter.sender.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

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
