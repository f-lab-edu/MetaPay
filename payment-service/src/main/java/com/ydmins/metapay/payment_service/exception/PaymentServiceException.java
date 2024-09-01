package com.ydmins.metapay.payment_service.exception;

public class PaymentServiceException extends RuntimeException{
    public PaymentServiceException(String message, Throwable cause){
        super(message, cause);
    }

    public PaymentServiceException(String message){
        super(message);
    }
}
