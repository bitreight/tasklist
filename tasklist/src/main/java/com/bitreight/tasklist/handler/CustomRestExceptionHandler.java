package com.bitreight.tasklist.handler;

import com.bitreight.tasklist.service.exception.ServiceException;
import com.bitreight.tasklist.service.exception.ServiceProjectAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceProjectNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskAlreadyExistsException;
import com.bitreight.tasklist.service.exception.ServiceTaskNotFoundException;
import com.bitreight.tasklist.service.exception.ServiceTaskVersionIsOutdatedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ServiceProjectNotFoundException.class, ServiceTaskNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(ServiceException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ServiceProjectAlreadyExistsException.class, ServiceTaskAlreadyExistsException.class,
            ServiceTaskVersionIsOutdatedException.class})
    public ResponseEntity<Object> handleConflict(ServiceException e) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, e.getLocalizedMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleInvalidRequestParameters(RuntimeException e) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException e) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "Access denied.");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
//        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
//            fieldErrors.put(error.getObjectName(), error.getDefaultMessage());
//        }

        ValidationError validationError = new ValidationError(HttpStatus.BAD_REQUEST, fieldErrors);
        return handleExceptionInternal(ex, validationError, headers, validationError.getStatus(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleAnyException(Exception e) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error happened while processing the request.");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

//    @Override
//    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
//                                                               HttpHeaders headers, HttpStatus status,
//                                                               WebRequest request) {
//        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Error happened while processing the request.");
//        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
//    }
}
