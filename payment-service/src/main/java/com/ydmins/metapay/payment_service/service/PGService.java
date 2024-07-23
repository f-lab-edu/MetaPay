package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.dto.PGResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PGService {

    public PGResponse requestPayment(PaymentRequest request){
        return PGResponse.builder()
                .isSuccess(true)
                .transactionId(UUID.randomUUID().toString())
                .amount(request.getAmount())
                .message("Payment processed successfully")
                .build();
    }
}
