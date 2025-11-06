package com.providences.events.shared.exception.exceptions;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final HttpStatus statusCode;

    public BusinessException(String msg, HttpStatus statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
