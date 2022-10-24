package com.example.jimmy_fturgunov.common.exceptions;


public class ObjectDoesNotExistsException extends RuntimeException {

    public ObjectDoesNotExistsException() {
    }

    public ObjectDoesNotExistsException(String message) {
        super(message);
    }

}
