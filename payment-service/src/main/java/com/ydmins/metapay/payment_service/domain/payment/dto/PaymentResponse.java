package com.ydmins.metapay.payment_service.domain.payment.dto;

import com.ydmins.metapay.payment_service.domain.payment.PaymentMethod;
import com.ydmins.metapay.payment_service.domain.payment.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentResponse {
    private Long id;
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private String errorMessage;
    private String userId;
    private String orderId;
}
