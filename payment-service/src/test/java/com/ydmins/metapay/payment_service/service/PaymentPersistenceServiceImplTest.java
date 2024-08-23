package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.domain.payment.PaymentMethod;
import com.ydmins.metapay.payment_service.domain.payment.PaymentStatus;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.exception.PaymentPersistenceException;
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

class PaymentPersistenceServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentPersistenceServiceImpl paymentPersistenceService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    private Payment createPayment(PaymentStatus status, String pg){
        return Payment.builder()
                .amount(new BigDecimal("100.00"))
                .method(PaymentMethod.CREDIT_CARD)
                .userId("userid")
                .orderId("12341234DA")
                .status(status)
                .paymentGateway(pg)
                .build();
    }

    private Payment getCapturedPayment(){
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());
        return paymentCaptor.getValue();
    }

    private void assertPaymentDetailsMatch(Payment payment, Payment savedPayment, PaymentStatus status, String pg){
        assertEquals(payment.getAmount(), savedPayment.getAmount());
        assertEquals(payment.getAmount(), savedPayment.getAmount());
        assertEquals(status, savedPayment.getStatus());
        assertEquals(payment.getMethod(), savedPayment.getMethod());
        assertEquals(payment.getUserId(), savedPayment.getUserId());
        assertEquals(payment.getOrderId(), savedPayment.getOrderId());
        assertEquals(pg, savedPayment.getPaymentGateway());
    }

    @Test
    void savePaymentSuccess() throws PaymentPersistenceException{
        // given
        Payment payment = createPayment(PaymentStatus.SUCCESSFUL, "PG");

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // when
        boolean result = paymentPersistenceService.savePayment(payment);

        // then
        assertTrue(result);

        Payment savedPayment = getCapturedPayment();
        assertPaymentDetailsMatch(payment, savedPayment, PaymentStatus.SUCCESSFUL, "PG");
        verify(paymentRepository).save(payment);
    }

    @Test
    void savePaymentFailure(){
        // given
        Payment payment = createPayment(PaymentStatus.SUCCESSFUL, "PG");

        when(paymentRepository.save(any(Payment.class))).thenThrow(new DataAccessException("Database error"){});

        // when & then
        assertThrows(PaymentPersistenceException.class, ()->{
            paymentPersistenceService.savePayment(payment);
        });
        verify(paymentRepository).save(payment);
    }
}