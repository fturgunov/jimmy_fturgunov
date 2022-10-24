package com.example.jimmy_fturgunov.common.exceptions;


public class NoAvailableCarsException extends RuntimeException {

    public NoAvailableCarsException() {
    }

    public NoAvailableCarsException(String message) {
        super(message);
    }

}
