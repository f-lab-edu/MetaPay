package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.domain.payment.PaymentMethod;
import com.ydmins.metapay.payment_service.domain.payment.PaymentStatus;
import com.ydmins.metapay.payment_service.domain.payment.dto.PGResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.handler.PaymentInconsistencyHandler;
import com.ydmins.metapay.payment_service.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final PGService pgService;
    private final PaymentInconsistencyHandler paymentInconsistencyHandler;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean processPayment(PaymentRequest request) {
        String idempotencyKey = request.getIdempotencyKey();
        if(isPaymentAlreadyProcessed(idempotencyKey)){
            return true;
        }
        Payment payment = createPaymentFromRequest(request);
        PGResponse pgResponse = processPGPayment(request, payment);
        savePayment(payment);
        return pgResponse!=null && pgResponse.isSuccess();
    }

    private boolean isPaymentAlreadyProcessed(String idempotencyKey){
        if(paymentRepository.existsByIdempotencyKey(idempotencyKey)){
            log.info("Payment with idempotency kdy {} already processed", idempotencyKey);
            return true;
        }
        return false;
    }

    private Payment createPaymentFromRequest(PaymentRequest request){
        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .method(PaymentMethod.CREDIT_CARD)
                .userId(request.getUserId())
                .orderId(request.getOrderId())
                .paymentGateway("PG")
                .idempotencyKey(request.getIdempotencyKey())
                .build();
        return payment;
    }

    private PGResponse processPGPayment(PaymentRequest request, Payment payment){
        try {
            PGResponse pgResponse = pgService.requestPayment(request);
            updatePaymentStatus(payment, pgResponse.isSuccess());
            return pgResponse;
        } catch (Exception e){
            log.error("Error processing payment", e);
            payment.setStatus(PaymentStatus.FAILED);
            return null;
        }
    }

    private void updatePaymentStatus(Payment payment, boolean isSuccess){
        payment.setStatus(isSuccess ? PaymentStatus.SUCCESSFUL : PaymentStatus.FAILED);
    }

    private void savePayment(Payment payment){
        try {
            paymentRepository.save(payment);
        }catch (DataIntegrityViolationException e){
            paymentInconsistencyHandler.handleInconsistency(payment);
            log.warn("Concurrent save detected for idempotency key : {}", payment.getIdempotencyKey());
        } catch (Exception e){
            pgService.cancelPayment(payment.getIdempotencyKey());
            log.error("Failed to save payment",e);
        }
    }
}
