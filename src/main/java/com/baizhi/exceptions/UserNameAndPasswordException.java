package com.baizhi.exceptions;

public class UserNameAndPasswordException extends RuntimeException {
    public UserNameAndPasswordException(String message) {
        super(message);
    }
}
