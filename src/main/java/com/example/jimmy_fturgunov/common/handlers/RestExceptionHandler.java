package com.example.jimmy_fturgunov.common.handlers;

import com.example.jimmy_fturgunov.common.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", "The input data is not valid.");
        ex.getBindingResult().getAllErrors().forEach((error) ->{

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValueNotUniqueException.class, ObjectDoesNotExistsException.class, UniqueIdFormatViolationException.class})
    protected ResponseEntity<Object> handleValueNotUniqueException(Exception ex) {
        return buildResponseEntity(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
    }

    @ExceptionHandler({NoAvailableCarsException.class, RequestedCarInUnavailableTimeFrameException.class})
    protected ResponseEntity<Object> handleNoAvailableResourcesException(Exception ex) {
        return buildResponseEntity(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(message);
    }
}
