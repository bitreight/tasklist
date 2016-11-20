package com.bitreight.tasklist.service.exception;

public class ServiceTaskAlreadyExistsException extends ServiceException {

    public ServiceTaskAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
