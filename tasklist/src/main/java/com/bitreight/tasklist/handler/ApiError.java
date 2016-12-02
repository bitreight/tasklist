package com.bitreight.tasklist.handler;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class ApiError {

    private String message;

    private HttpStatus status;

    private List<String> errors;

    public ApiError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.errors = Collections.singletonList(message);
    }

    public ApiError(String message, HttpStatus status, List<String> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<String> getErrors() {
        return errors;
    }
}
