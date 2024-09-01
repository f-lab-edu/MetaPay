package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.dto.GetPaymentResponse;
import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import com.ydmins.metapay.payment_service.exception.PaymentProcessingException;

public interface PaymentService {

    boolean processPayment(PaymentRequest request) throws PaymentProcessingException;

    GetPaymentResponse getPaymentDetail(Long paymentId);
}
