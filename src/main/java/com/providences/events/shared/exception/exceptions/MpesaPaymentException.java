package com.providences.events.shared.exception.exceptions;

public class MpesaPaymentException extends RuntimeException {
    private int statusCode;

    public MpesaPaymentException(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}