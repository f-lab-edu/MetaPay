package com.ydmins.metapay.payment_service.service.mapper;

import com.ydmins.metapay.payment_service.domain.payment.Payment;
import com.ydmins.metapay.payment_service.domain.payment.dto.GetPaymentResponse;

public class PaymentMapper {

    public static GetPaymentResponse toGetPaymentResponse(Payment payment){
        return GetPaymentResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .method(payment.getMethod())
                .paymentGateway(payment.getPaymentGateway())
                .userId(payment.getUserId())
                .orderId(payment.getOrderId())
                .build();
    }
}
