package com.ydmins.metapay.payment_service.exception;

public class PaymentPersistenceException extends RuntimeException {
    public PaymentPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}