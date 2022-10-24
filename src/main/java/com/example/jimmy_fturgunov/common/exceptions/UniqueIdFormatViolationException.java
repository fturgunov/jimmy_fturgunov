package com.example.jimmy_fturgunov.common.exceptions;


public class UniqueIdFormatViolationException extends RuntimeException {

    public UniqueIdFormatViolationException() {
    }

    public UniqueIdFormatViolationException(String message) {
        super(message);
    }

}
