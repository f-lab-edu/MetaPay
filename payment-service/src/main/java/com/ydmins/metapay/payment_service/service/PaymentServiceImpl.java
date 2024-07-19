package com.ydmins.metapay.payment_service.service;

import com.ydmins.metapay.payment_service.domain.payment.dto.PaymentRequest;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Override
    public boolean processPayment(PaymentRequest request) {
        return false;
    }
}
