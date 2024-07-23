package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.domain.payment.PaymentMethod;
import com.ydmins.metapay.payment_service.domain.payment.PaymentStatus;
import com.ydmins.metapay.payment_service.domain.payment.dto.PGResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final PGService pgService;

    @Override
    public boolean processPayment(PaymentRequest request) {
        Payment payment = createPaymentFromRequest(request);
        PGResponse pgResponse = null;
        try {
            pgResponse = pgService.requestPayment(request);
        } catch (Exception e){
            log.error("Error processing payment", e);
            payment.setStatus(PaymentStatus.FAILED);
        }
        if (pgResponse != null){
            if(pgResponse.isSuccess()){
                payment.setStatus(PaymentStatus.SUCCESSFUL);
            } else {
                payment.setStatus(PaymentStatus.FAILED);
            }
        }
        try {
            paymentRepository.save(payment);
        } catch (Exception e){
            log.error("Failed to save payment",e);
        }
        if(pgResponse == null){
            return false;
        }
        return pgResponse.isSuccess();
    }

    private Payment createPaymentFromRequest(PaymentRequest request){
        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .method(PaymentMethod.CREDIT_CARD)
                .userId(request.getUserId())
                .orderId(request.getOrderId())
                .paymentGateway("PG")
                .build();
        return payment;
    }
}
