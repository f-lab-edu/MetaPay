package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.domain.payment.dto.PGResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.exception.PGCommunicationException;
import com.ydmins.metapay.payment_service.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
public class PGService {

    public PGResponse requestPayment(PaymentRequest request) throws PGCommunicationException{
        try {
            return pgResponseBuilder(request.getAmount(), true, "Payment processed successfully");
        } catch (Exception e){
            // 추후 예외상황 자세히 수정가능
            log.error("Payment Processing failed: {}", e.getMessage());
            throw new PGCommunicationException("PG Communication failed", e);
        }
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
