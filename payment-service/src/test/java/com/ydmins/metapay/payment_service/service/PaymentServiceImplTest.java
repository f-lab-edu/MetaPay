package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.domain.payment.PaymentMethod;
import com.ydmins.metapay.payment_service.domain.payment.PaymentStatus;
import com.ydmins.metapay.payment_service.domain.payment.dto.PGResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PGService pgService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

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
                .idempotencyKey("12345")
                .build();
    }

    private PGResponse createPGResponse(boolean isSuccess, String message, String idempotencyKey){
        return PGResponse.builder()
                .isSuccess(isSuccess)
                .transactionId("12345")
                .amount(new BigDecimal("100.00"))
                .message(message)
                .idempotencyKey(idempotencyKey)
                .build();
    }

    private Payment getCapturedPayment(){
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());
        return paymentCaptor.getValue();
    }

    private void assertPaymentDetailsMatch(PaymentRequest request, Payment savedPayment, PaymentStatus status,
                                          String pg){
        assertEquals(request.getAmount(), savedPayment.getAmount());
        assertEquals(request.getAmount(), savedPayment.getAmount());
        assertEquals(status, savedPayment.getStatus());
        assertEquals(request.getMethod(), savedPayment.getMethod());
        assertEquals(request.getUserId(), savedPayment.getUserId());
        assertEquals(request.getOrderId(), savedPayment.getOrderId());
        assertEquals(pg, savedPayment.getPaymentGateway());
    }

    @Test
    void processPaymentSuccess() {
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = createPGResponse(true,"Payment processed successfully", request.getIdempotencyKey());

        when(paymentRepository.existsByIdempotencyKey(request.getIdempotencyKey())).thenReturn(false);
        when(pgService.requestPayment(request)).thenReturn(pgResponse);

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertTrue(result);

        verify(paymentRepository).save(any(Payment.class));
        // Captured Payment
        Payment savedPayment = getCapturedPayment();
        assertPaymentDetailsMatch(request, savedPayment, PaymentStatus.SUCCESSFUL, "PG");
    }

    @Test
    void processPaymentAlreadyProcessed() {
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = createPGResponse(true,"Payment processed successfully", request.getIdempotencyKey());

        when(paymentRepository.existsByIdempotencyKey(anyString())).thenReturn(true);
        when(pgService.requestPayment(request)).thenReturn(pgResponse);

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertTrue(result);
    }

    @Test
    public void processPaymentFailure(){
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = createPGResponse(false, "Payment failed", request.getIdempotencyKey());

        when(paymentRepository.existsByIdempotencyKey(anyString())).thenReturn(false);
        when(pgService.requestPayment(request)).thenReturn(pgResponse);

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertFalse(result);

        // Captured Payment
        Payment savedPayment = getCapturedPayment();
        assertPaymentDetailsMatch(request, savedPayment, PaymentStatus.FAILED, "PG");
    }

    @Test
    public void processPaymentPGServiceException(){
        // given
        PaymentRequest request = createPaymentRequest();

        when(paymentRepository.existsByIdempotencyKey(anyString())).thenReturn(false);
        when(pgService.requestPayment(request)).thenThrow(new RuntimeException("Payment gateway error"));

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertFalse(result);

        // Captured Payment
        Payment savedPayment = getCapturedPayment();
        assertPaymentDetailsMatch(request, savedPayment, PaymentStatus.FAILED, "PG");
    }

    @Test
    public void processPaymentPaymentRepositoryException(){
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = createPGResponse(true, "Payment proccssed successfully", request.getIdempotencyKey());

        when(paymentRepository.existsByIdempotencyKey(anyString())).thenReturn(false);
        when(pgService.requestPayment(request)).thenReturn(pgResponse);
        doThrow(new DataAccessException("Database error"){}).when(paymentRepository).save(any(Payment.class));

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertTrue(result);

        // Captured Payment
        Payment savedPayment = getCapturedPayment();
        assertPaymentDetailsMatch(request, savedPayment, PaymentStatus.SUCCESSFUL, "PG");
    }
}