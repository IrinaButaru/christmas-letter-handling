package com.christmas.letter.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String UNAUTHORIZED_CODE = "not.authorized";
    public static final String UNAUTHORIZED_MESSAGE = "Request is unauthorized";
}
