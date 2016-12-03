package com.bitreight.tasklist.handler;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ValidationError {

    private HttpStatus status;

    private Map<String, String> fieldErrors;

    public ValidationError(HttpStatus status, Map<String, String> fieldErrors) {
        this.status = status;
        this.fieldErrors = fieldErrors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
