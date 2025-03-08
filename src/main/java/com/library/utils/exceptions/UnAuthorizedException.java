package com.library.utils.exceptions;

public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException(String message) {
        super(message);
    }
    public UnAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

}
