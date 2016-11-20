package com.bitreight.tasklist.service.exception;

public class ServiceUserAlreadyExistsException extends ServiceException {

    public ServiceUserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
