package com.bitreight.tasklist.service.exception;

public class ServiceTaskVersionIsOutdatedException extends ServiceException {

    public ServiceTaskVersionIsOutdatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
