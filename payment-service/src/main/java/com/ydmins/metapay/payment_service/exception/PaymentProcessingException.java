package com.ydmins.metapay.payment_service.exception;

public class PaymentProcessingException extends Exception{
    public PaymentProcessingException(String message, Throwable cause){
        super(message, cause);
    }

    public PaymentProcessingException(String message){
        super(message);
    }
}
