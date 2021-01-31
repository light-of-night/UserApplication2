package com.baizhi.controller;

import com.baizhi.entities.ErrorMessage;
import com.baizhi.exceptions.UserNameAndPasswordException;
import com.baizhi.exceptions.VerifyCodeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //@ControllerAdvice +@ResponseBody
public class CustomExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = UserNameAndPasswordException.class)
    public ErrorMessage handlerError(UserNameAndPasswordException ex) {
        return new ErrorMessage(1001, ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = VerifyCodeException.class)
    public ErrorMessage handlerError(VerifyCodeException ex) {
        return new ErrorMessage(1002, ex.getMessage());
    }
}
