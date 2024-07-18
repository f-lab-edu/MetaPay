package com.ydmins.metapay.payment_service.domain.payment;

import com.ydmins.metapay.payment_service.domain.common.TimeBaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Payment extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    private String errorMessage;
    private String paymentGateway;

    private String userId;
    private String orderId;
}