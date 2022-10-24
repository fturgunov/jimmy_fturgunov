package com.example.jimmy_fturgunov.common.exceptions;


public class RequestedCarInUnavailableTimeFrameException extends RuntimeException {

    public RequestedCarInUnavailableTimeFrameException() {
    }

    public RequestedCarInUnavailableTimeFrameException(String message) {
        super(message);
    }

}
