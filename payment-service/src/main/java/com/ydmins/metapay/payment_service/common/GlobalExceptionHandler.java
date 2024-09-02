package com.ydmins.metapay.payment_service.common;

import com.ydmins.metapay.payment_service.exception.PaymentNotFoundException;
import com.ydmins.metapay.payment_service.exception.PaymentProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static com.ydmins.metapay.payment_service.common.PaymentMessages.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentProcessingException.class)
    public ApiResponse<?> handlePaymentProcessingException(PaymentProcessingException e){
        log.error(PAYMENT_PROCESSING_ERROR, e);
        return ApiResponse.error(500, UNEXPECTED_ERROR, null);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ApiResponse<?> handlePaymentNotFoundException(PaymentNotFoundException e){
        log.error(PAYMENT_NOT_FOUND, e);
        return ApiResponse.notFound(404, PaymentMessages.PAYMENT_NOT_FOUND, null);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleUnexpectedException(Exception e){
        log.error(UNEXPECTED_ERROR, e);
        return ApiResponse.error(500, UNEXPECTED_ERROR, null);
    }
}
