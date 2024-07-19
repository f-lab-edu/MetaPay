package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;

public interface PaymentService {

    boolean processPayment(PaymentRequest request);
}
