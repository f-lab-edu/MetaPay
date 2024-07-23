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
    private PGGatewayService pgGatewayService;

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
                .build();
    }

    @Test
    void processPayment_Success() {
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = PGResponse.builder()
                .isSuccess(true)
                .transactionId("12345")
                .amount(new BigDecimal("100.00"))
                .message("Payment processed successfully")
                .build();

        when(pgGatewayService.requestPayment(request)).thenReturn(pgResponse);

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertTrue(result);

        // ArgumentCaptor
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());

        // Captured Payment
        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(request.getAmount(), savedPayment.getAmount());
        assertEquals(request.getMethod(), savedPayment.getMethod());
        assertEquals(PaymentStatus.SUCCESSFUL, savedPayment.getStatus());
        assertEquals(request.getUserId(), savedPayment.getUserId());
        assertEquals(request.getOrderId(), savedPayment.getOrderId());
        assertEquals("PG", savedPayment.getPaymentGateway());
    }

    @Test
    public void processPayment_Failure(){
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = PGResponse.builder()
                .isSuccess(false)
                .transactionId("12345")
                .amount(new BigDecimal("100.00"))
                .message("Payment failed")
                .build();

        when(pgGatewayService.requestPayment(request)).thenReturn(pgResponse);

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertFalse(result);

        // ArgumentCaptor
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());

        // Captured Payment
        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(request.getAmount(), savedPayment.getAmount());
        assertEquals(request.getAmount(), savedPayment.getAmount());
        assertEquals(PaymentStatus.FAILED, savedPayment.getStatus());
        assertEquals(request.getMethod(), savedPayment.getMethod());
        assertEquals(request.getUserId(), savedPayment.getUserId());
        assertEquals(request.getOrderId(), savedPayment.getOrderId());
        assertEquals("PG", savedPayment.getPaymentGateway());
    }

    @Test
    public void processPayment_PGGatewayServiceException(){
        // given
        PaymentRequest request = createPaymentRequest();

        when(pgGatewayService.requestPayment(request)).thenThrow(new RuntimeException("Payment gateway error"));

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertFalse(result);

        // ArgumentCaptor
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());

        // Captured Payment
        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(request.getAmount(), savedPayment.getAmount());
        assertEquals(PaymentStatus.FAILED, savedPayment.getStatus());
        assertEquals(request.getMethod(), savedPayment.getMethod());
        assertEquals(request.getUserId(), savedPayment.getUserId());
        assertEquals(request.getOrderId(), savedPayment.getOrderId());
        assertEquals("PG", savedPayment.getPaymentGateway());
    }

    @Test
    public void processPayment_PaymentRepositoryException(){
        // given
        PaymentRequest request = createPaymentRequest();
        PGResponse pgResponse = PGResponse.builder()
                .isSuccess(true)
                .transactionId("12345")
                .amount(new BigDecimal("100.00"))
                .message("Payment processed successfully")
                .build();

        when(pgGatewayService.requestPayment(request)).thenReturn(pgResponse);
        doThrow(new DataAccessException("Database error"){}).when(paymentRepository).save(any(Payment.class));

        // when
        boolean result = paymentService.processPayment(request);

        // then
        assertTrue(result);

        // ArgumentCaptor
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());

        // Captured Payment
        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(request.getAmount(), savedPayment.getAmount());
        assertEquals(PaymentStatus.SUCCESSFUL, savedPayment.getStatus());
        assertEquals(request.getMethod(), savedPayment.getMethod());
        assertEquals(request.getUserId(), savedPayment.getUserId());
        assertEquals(request.getOrderId(), savedPayment.getOrderId());
        assertEquals("PG", savedPayment.getPaymentGateway());
    }
}