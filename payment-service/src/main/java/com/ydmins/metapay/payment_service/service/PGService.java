package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.domain.payment.dto.PGResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
public class PGService {

    public PGResponse requestPayment(PaymentRequest request){
        return pgResponseBuilder(request.getAmount(), true, "Payment processed successfullly");
    }


    public PGResponse checkPaymentStatus(Payment payment){
        boolean isSuccess = payment.getIdempotencyKey().hashCode() % 2 ==0;
        String message = isSuccess ? "Payment Successful" : "Payment failed";
        return pgResponseBuilder(payment.getAmount(), isSuccess, message);
    }

    private PGResponse pgResponseBuilder(BigDecimal amount, boolean isSuccess, String message){
        return PGResponse.builder()
                .isSuccess(isSuccess)
                .amount(amount)
                .message(message)
                .build();
    }

    public boolean cancelPayment(String idempotencyKey){
        log.info("Payment cancellation requested for idempotencyKye : {}", idempotencyKey);
        return true;
    }
}
