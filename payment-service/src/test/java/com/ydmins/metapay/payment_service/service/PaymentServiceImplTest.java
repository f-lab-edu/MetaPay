package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.domain.payment.PaymentMethod;
import com.ydmins.metapay.payment_service.domain.payment.PaymentStatus;
import com.ydmins.metapay.payment_service.domain.payment.dto.PGResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.exception.PGCommunicationException;
import com.ydmins.metapay.payment_service.exception.PaymentPersistenceException;
import com.ydmins.metapay.payment_service.exception.PaymentProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {
    @Mock
    private PGService pgService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentPersistenceService paymentPersistenceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private PaymentRequest createPaymentRequest(){
        return PaymentRequest.builder()
                .amount(new BigDecimal("100.00"))
                .method(PaymentMethod.CREDIT_CARD)
                .userId("userid")
                .orderId("12341234DA")
                .build();
    }

    private PGResponse createPGResponse(boolean isSuccess, String message){
        return PGResponse.builder()
                .isSuccess(isSuccess)
                .transactionId("12345")
                .amount(new BigDecimal("100.00"))
                .message(message)
                .build();
    }

    @Test
    void processPaymentSuccess() throws PaymentProcessingException {
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = createPGResponse(true, "Payment processed successfully");

        when(pgService.requestPayment(eq(request))).thenReturn(pgResponse);
        when(paymentPersistenceService.savePayment(any(Payment.class))).thenReturn(true);

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertTrue(result);

        verify(pgService).requestPayment(any(PaymentRequest.class));
        verify(paymentPersistenceService).savePayment(argThat(payment -> payment.getStatus() == PaymentStatus.SUCCESSFUL));
    }

    @Test
    void processPaymentPGCommunicationFailure() throws PaymentProcessingException, PGCommunicationException {
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = createPGResponse(false, "Payment request failed due to an unexpected error");

        when(pgService.requestPayment(eq(request))).thenThrow(new PGCommunicationException("PG Communication failed"));
        when(paymentPersistenceService.savePayment(any(Payment.class))).thenReturn(true);

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertFalse(result);

        verify(pgService).requestPayment(any(PaymentRequest.class));
        verify(paymentPersistenceService).savePayment(argThat(payment -> payment.getStatus() == PaymentStatus.FAILED));
    }

    @Test
    void processPaymentPersistenceFailure() throws PaymentProcessingException, PGCommunicationException{
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = createPGResponse(false, "Payment processed successfully");

        when(pgService.requestPayment(eq(request))).thenReturn(pgResponse);
        when(paymentPersistenceService.savePayment(any(Payment.class))).thenThrow(new PaymentPersistenceException(
                "Failed to save payment result"));

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertFalse(result);

        verify(pgService).requestPayment(any(PaymentRequest.class));
        verify(paymentPersistenceService).savePayment(argThat(payment -> payment.getStatus() == PaymentStatus.FAILED));
    }

    @Test
    void processPaymentUnexpectedFailureAtPGCommunication(){
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = mock(PGResponse.class);

        when(pgService.requestPayment(eq(request))).thenThrow(new RuntimeException(
                "An unexpected error occurred. Please contact support"));

        // when
        assertThrows(PaymentProcessingException.class, () -> paymentService.processPayment(request));

        verify(pgService).requestPayment(any(PaymentRequest.class));
    }

    @Test
    void processPaymentUnexpectedFailureAtPaymentPersistence(){
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = mock(PGResponse.class);

        when(pgService.requestPayment(eq(request))).thenThrow(new RuntimeException(
                "An unexpected error occurred. Please contact support"));

        // when
        assertThrows(PaymentProcessingException.class, () -> paymentService.processPayment(request));

        verify(pgService).requestPayment(any(PaymentRequest.class));
    }
}