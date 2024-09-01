package com.ydmins.metapay.payment_service.exception;

public class PaymentNotFoundException extends RuntimeException{
    public PaymentNotFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public PaymentNotFoundException(String message){
        super(message);
    }
}
