package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.dto.PGResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;

import java.util.UUID;

public class PGGatewayService {

    public PGResponse requestPayment(PaymentRequest request){
        return PGResponse.builder()
                .success(true)
                .transactionId(UUID.randomUUID().toString())
                .amount(request.getAmount())
                .message("Payment processed successfully")
                .build();
    }
}
