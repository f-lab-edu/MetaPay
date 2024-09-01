package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.domain.payment.PaymentMethod;
import com.ydmins.metapay.payment_service.domain.payment.PaymentStatus;
import com.ydmins.metapay.payment_service.domain.payment.dto.GetPaymentResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PGResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.exception.*;
import com.ydmins.metapay.payment_service.service.mapper.PaymentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PGService pgService;
    private final PaymentPersistenceService paymentPersistenceService;

    @Override
    public boolean processPayment(PaymentRequest request) throws PaymentProcessingException{
        Payment payment = createPaymentFromRequest(request);
        try{
            PGResponse pgResponse = pgService.requestPayment(request);
            payment.setStatus(pgResponse.isSuccess() ? PaymentStatus.SUCCESSFUL : PaymentStatus.FAILED);
            return paymentPersistenceService.savePayment(payment);
        } catch (PGCommunicationException e){
            handlePaymentProcessingFailure(payment, e);
        } catch (PaymentPersistenceException e){
            handlePaymentPersistenceFailure(payment, e);
        } catch (Exception e) {
            log.error("Unexpected error during payment processing", e);
            throw new PaymentProcessingException("An unexpected error occurred. Please contact support", e);
        }
        return false;
    }

    @Override
    public GetPaymentResponse getPaymentDetail(Long paymentId) {
        try {
            Payment payment = paymentPersistenceService.findPayment(paymentId);
            return PaymentMapper.toGetPaymentResponse(payment);
        } catch(PaymentNotFoundException e) {
            log.error("Payment detail not found with paymentId: "+paymentId, e);
            throw new PaymentNotFoundException("Payment detail not found with paymentId: "+paymentId);
        } catch(PaymentPersistenceException e){
            log.error("Payment not found for payment id : ", paymentId, e);
            throw new PaymentPersistenceException("Payment not found", e);
        } catch(Exception e){
            log.error("Unexpected error occurred while getting payment details for id: {}", paymentId, e);
            throw new PaymentServiceException("Unexpected error occurred", e);
        }
    }

    private Payment createPaymentFromRequest(PaymentRequest request){
        Payment payment = Payment.builder()
                .amount(request.getAmount())
                .method(PaymentMethod.CREDIT_CARD)
                .userId(request.getUserId())
                .orderId(request.getOrderId())
                .paymentGateway("PG")
                .status(PaymentStatus.PENDING)
                .build();
        return payment;
    }

    private boolean handlePaymentProcessingFailure(Payment payment, PGCommunicationException e){
        log.error(e.getMessage(), e);
        payment.setStatus(PaymentStatus.FAILED);
        paymentPersistenceService.savePayment(payment);
        return false;
    }

    private boolean handlePaymentPersistenceFailure(Payment payment, PaymentPersistenceException e){
        log.error(e.getMessage(), e);
        cancelPaymentOnSaveFailure(payment);
        return false;
    }

    private void cancelPaymentOnSaveFailure(Payment payment){
        try{
            pgService.cancelPayment(payment.getIdempotencyKey());
        } catch (Exception e){
            log.error("CRITICAL : Failed to cancel payment after save failure");
        }
    }
}
