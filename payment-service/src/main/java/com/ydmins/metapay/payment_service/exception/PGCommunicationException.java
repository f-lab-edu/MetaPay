package com.ydmins.metapay.payment_service.exception;

public class PGCommunicationException extends RuntimeException{
    public PGCommunicationException(String message, Throwable cause){
        super(message, cause);
    }

    public PGCommunicationException(String message){
        super(message);
    }
}
