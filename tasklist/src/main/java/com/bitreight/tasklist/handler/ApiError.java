package com.bitreight.tasklist.handler;

import org.springframework.http.HttpStatus;

public class ApiError {

    private HttpStatus status;

    private String message;

    public ApiError(HttpStatus status, String message) {
        this.message = message;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
